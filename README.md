# Практическая работа #4
## Интерцепторы. Создание небольшого CRUD-сервиса «Список задач».
## Выполнил: Туев Д. ЭФМО - 01

## Описание проекта
Данный проект предназначен для изучения основ построения web приложений на основе *chi* роутера.

## Цели:  
1.	Научиться строить REST-маршруты и обрабатывать методы GET/POST/PUT/DELETE.
2.	Реализовать небольшой CRUD-сервис «ToDo» (без БД, хранение в памяти).
3.	Добавить простые интерцепторы (логирование, CORS).
4.	Научиться тестировать API запросами через curl/Postman/HTTPie.

## Технологии

В проекте используются следующие технологии и инструменты:

- **Spring Boot 3.5.6** — основной фреймворк приложения
- **Spring Web** — для разработки REST API
- **Spring Validation** — валидация входных данных с использованием Jakarta Validation
- **Lombok** — сокращение шаблонного кода (геттеры, сеттеры, конструкторы)
- **Maven** — управление зависимостями и сборка проекта
- **Java 17** — язык программирования
- **ConcurrentHashMap** — потокобезопасное хранилище данных в памяти
- **Jackson** — сериализация/десериализация JSON

## Ход работы
### Структура проекта

```
MIREA-FFJ-Practice-4/
├── src/
│   └── main/
│       └── java/my/learn/mireaffjpractice4/
│           ├── MireaFfjPractice4Application.java
│           ├── config/
│           │   └── WebConfig.java
│           ├── controller/
│           │   ├── MainController.java
│           │   └── TaskController.java
│           ├── dto/
│           │   ├── request/
│           │   │   ├── CreateTaskRequest.java
│           │   │   └── UpdateTaskRequest.java
│           │   └── response/
│           │       ├── StatusDTO.java
│           │       └── TaskDTO.java
│           ├── exception/
│           │   ├── AppException.java
│           │   ├── AppExceptionHandler.java
│           │   ├── InvalidArgumentException.java
│           │   └── NotFoundException.java
│           ├── interceptor/
│           │   └── LoggingInterceptor.java
│           ├── model/
│           │   └── Task.java
│           ├── repository/
│           │   ├── TaskRepository.java
│           │   └── impl/
│           │       └── InMemoryTaskRepositoryImpl.java
│           └── service/
│               ├── TaskService.java
│               └── impl/
│                   └── TaskServiceImpl.java
├── pom.xml
└── README.md
```

# Для разработчика

## Запросы - ответы
Сервер поддерживает следующие [запросы](https://lively-flare-564043.postman.co/workspace/My-Workspace~fe2081e8-b325-4776-8b48-400d41f5b4bd/collection/42992055-41a9fb3c-2aa9-4e24-b8ce-07b1e3503fc8?action=share&source=copy-link&creator=42992055)
по эндпоинту /api/v1 (на данный момент сервер поддерживает версии api: v1):
- GET /health — возвращает {"status":"ok"}.
- GET /tasks — возвращает список задач, поддерживает фильтр ?page= &limit= , а также фильтр ?done=.
- POST /tasks — создаёт задачу по {"title":"..."}.
- GET /tasks/{id} — возвращает одну задачу.
- PUT /tasks/{id} — для изменения флага done на "true", в теле необходимо передать заголовок и статус задачи.
- DELETE /tasks/{id} — для удаления задачи из Repo по id, возвращает удаляемую задачу.


## Проверка работоспоcобности
### Обработка ошибок и коды ответа

| Код | Название | Где обрабатывается | Что значит / когда возвращается |
| :-- | :-- | :-- | :-- |
| 200 | OK | `getTasks`, `getTaskById`, `updateTaskById` | Успешный ответ с данными (список задач, одна задача или обновлённая задача). |
| 201 | Created | `createTask` | Новая задача успешно создана, в теле возвращается JSON с задачей. |
| 204 | No Content | `deleteTask` | Задача успешно удалена, тело ответа пустое. |
| 400 | Bad Request | `getTasks`, `createTask`, `updateTaskById` | Некорректные данные запроса: неверный ID, пустой или некорректный JSON, ошибка в query-параметре. |
| 404 | Not Found | `getTaskById`, `updateTaskById`, `deleteTask` | Задача с указанным ID не найдена. |
| 422 | Unprocessable Entity | `createTask`, `updateTaskById` | Нарушена валидация данных (title короче 3 или длиннее 100 символов). |

### Примеры запросов
`GET /tasks` без параметров

![img_3.png](img_3.png)

`POST /tasks` успешная валидация

![img.png](img.png)

`POST /tasks` ошибка валидации

![img_1.png](img_1.png)

`GET /tasks/{id}`

![img_4.png](img_4.png)

`PUT /tasks/{id}`

![img_2.png](img_2.png)

`DELETE /tasks/{id}`

![img_7.png](img_7.png)

`GET /tasks` с фильтром по done

![img_6.png](img_6.png)

`GET /tasks` с пагинацией

![img_5.png](img_5.png)

### Пример тестирования

| Маршрут                | Запрос (пример)                                | Ожидаемый ответ                   | Фактический ответ                             |
|------------------------|------------------------------------------------|-----------------------------------|-----------------------------------------------|
| **POST /tasks**        | `{"title":"Задача 1"}`                         | 201 Created + JSON новой задачи   | 201, задача создана                           |
| **POST /tasks**        | `{"title":"3a"}`                               | 400 Bad request                   | 400, `{"message": "Validation failed","errors": {"title": "размер должен находиться в диапазоне от 3 до 100"},"status": 400}` |
| **GET /tasks**         | без параметров                                 | 200 OK + полный список задач      | 200, список JSON                              |
| **GET /tasks**         | `?done=true`                                   | 200 OK + список выполненных задач | 200, JSON только с `done=true`                |
| **GET /tasks**         | `?page=1&limit=2`                              | 200 OK + первые 2 задачи          | 200, вернулись задачи 1 и 3                   |
| **GET /tasks/{id}**    | `/tasks/7`                                     | 200 OK + JSON задачи              | 200, вернулась задача id=7                    |
| **GET /tasks/{id}**    | `/tasks/999` (несуществующий)                  | 404 Not Found                     | 404, `{"error":"task not found"}`             |
| **PUT /tasks/{id}**    | `/tasks/7` + `{"title":"qwerty","done":true}`  | 200 OK + обновлённая задача       | 200, обновление прошло                        |
| **PUT /tasks/{id}**    | `/tasks/999` + `{"title":"Fail","done":true}`  | 404 Not Found                     | 404, `{"error":"task not found"}`             |
| **DELETE /tasks/{id}** | `/tasks/2`                                     | 204 No Content                    | 204, задача удалена                           |
| **DELETE /tasks/{id}** | `/tasks/999`                                   | 404 Not Found                     | 404, `{"error":"task not found"}`             |


## Выводы

В ходе работы был реализован HTTP-сервер на Java с использованием фреймворка **Spring Boot**.
Были обработаны все основные маршруты CRUD для ресурса `tasks`: создание, чтение, обновление и удаление.
Подключены и протестированы интерцепторы для логирования запросов.

Кроме обязательной части, были выполнены и дополнительные задания:

1. **Валидация длины title** — добавлена проверка через Jakarta Validation, что название задачи не короче 3 и не длиннее 100 символов. При нарушении возвращается ошибка `422 Unprocessable Entity`.
2. **Пагинация списка** — реализованы параметры `page` и `limit` для запроса `/tasks`, позволяющие получать данные постранично.
3. **Фильтрация по done** — добавлен параметр `done=true/false`, чтобы выбирать только выполненные или только невыполненные задачи.
4. **Версионирование API** — все маршруты вынесены под префикс `/api/v1/...`, что позволит в будущем поддерживать несколько версий API параллельно.
5. **Обработка исключений** — реализован глобальный обработчик исключений `AppExceptionHandler` для централизованной обработки ошибок.
6. **Архитектура приложения** — проект организован по слоям (controller, service, repository, dto), что обеспечивает разделение ответственности и упрощает поддержку кода.

Наиболее сложными моментами оказались:

- Реализация пагинации с учётом потокобезопасности при работе с `ConcurrentHashMap`
- Настройка валидации с использованием Jakarta Validation
- Организация централизованной обработки ошибок через `@RestControllerAdvice`

Проект демонстрирует применение современных подходов к разработке REST API на Spring Boot, включая использование DTO для разделения моделей данных и представления, применение паттерна Repository для абстракции работы с данными, и использование интерцепторов для кросс-функциональных задач (логирование).











