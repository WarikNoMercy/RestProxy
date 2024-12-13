# Proxy REST API с кешированием и аудитом

Этот проект предоставляет REST API, которое работает как прокси для перенаправления запросов к внешним сервисам. В нем реализованы такие функции, как доступ к конкретным эндпоинтам для пользователей, кеширование ответов, ведение аудита взаимодействий и сохранение записей аудита в базе данных. Приложение спроектировано с учетом безопасности и предоставляет управление доступом на основе ролей (RBAC) для различных ресурсов.

## Особенности

- **Проксирование запросов:** Перенаправляет запросы к внешним API (`/posts`, `/users`, `/albums`).
- **Кеширование:** Ответы на `GET` запросы кешируются для повышения производительности с использованием механизмов кеширования Spring.
- **Контроль доступа на основе ролей:** Разные роли пользователей (`ADMIN`, `POSTS_ADMIN`, `POSTS_VIEWER` и т.д.) могут получать доступ к определенным эндпоинтам.
- **Логирование аудита:** Все запросы сохраняются в базу данных для целей аудита. 
- **Обработка исключений:** Ошибки доступа (403) обрабатываются корректно.

## Установка приложения

1. **Клонируйте репозиторий:**
   ```bash
   git clone https://github.com/yourusername/rest-api-proxy.git
   cd rest-api-proxy
   

2. Настройка приложения

Обновите файл `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.security.user.name=admin
spring.security.user.password=admin_password
spring.host.url=http://external-service-url
```

3. Соберите приложение с помощью maven

  ```bash
  mvn clean install
  mvn spring-boot:run
```

## Использование API


### 1. Управление пользователями

Для регистрации нового пользователя :

```bash
POST /api/new-user HTTP/1.1
Content-Type: application/json

{
    "username": "new_user",
    "password": "user_password",
    "role": "user_role"
}
```

### 2. Запрос по endpoint

Пользователи с соответствующими ролями могут обратиться по своим endpoint:
  Например пользователь с ролью albums можешь выполнить запрос:
  ```
  GET /api/albums
  ```
Точно также для пользователей, с соответствующими им ролями. 
