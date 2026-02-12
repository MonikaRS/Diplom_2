# 🍔 Stellar Burgers API Автотесты

## 📋 Описание проекта
Автотесты для API сервиса Stellar Burgers. Проверяют функциональность создания пользователя, авторизации и создания заказов.

## 🛠 Технологии
- Java 11
- Maven
- JUnit 4
- REST Assured
- Allure Report
- Gson

## 📁 Структура проекта
src/test/java/org/stellar/
├── clients/ # API клиенты
│ ├── BaseApiClient.java
│ ├── UserApiClient.java
│ └── OrderApiClient.java
├── models/ # Модели данных
│ └── User.java
└── tests/ # Тесты
├── BaseTest.java
├── UserCreationTest.java
├── UserLoginTest.java
└── OrderCreationTest.java

text

## ✅ Тест-кейсы

### Создание пользователя
- ✅ Создание уникального пользователя
- ✅ Создание уже зарегистрированного пользователя
- ✅ Создание пользователя без email
- ✅ Создание пользователя без пароля
- ✅ Создание пользователя без имени

### Авторизация
- ✅ Вход под существующим пользователем
- ✅ Вход с неверным email
- ✅ Вход с неверным паролем
- ✅ Вход с пустым email
- ✅ Вход с пустым паролем

### Создание заказа
- ✅ С авторизацией и ингредиентами
- ❌ Без авторизации (БАГ API - возвращает 200 вместо 401)
- ✅ Без ингредиентов
- ✅ С неверным хешем ингредиента
- ✅ С одним ингредиентом

## 🚀 Запуск тестов

### Без отчета:
```bash
mvn clean test
С Allure отчетом:
bash
# 1. Запустить тесты
mvn clean test

# 2. Сгенерировать отчет
allure generate target/allure-results -o target/allure-report --clean

# 3. Открыть отчет
allure open target/allure-report
📊 Allure отчет
Архив с результатами тестов: allure-results.tar.gz

Чтобы посмотреть отчет:

bash
tar -xzf allure-results.tar.gz
allure serve target/allure-results/
🐛 Найденные баги
Баг #1: Создание заказа без авторизации
Ожидалось: 401 Unauthorized
Фактически: 200 OK
Тест: OrderCreationTest.shouldNotCreateOrderWithoutAuth()
Приоритет: CRITICAL

📦 Требования
Java 11+

Maven 3.6+

Allure Commandline (для отчета)

📝 Примечания
Все тесты независимы

Пользователи создаются в @Before и удаляются в @After

Данные генерируются динамически (нет захардкоженных значений)

Используется сериализация через Gson

HTTP статус-коды через HttpStatus константы

👤 Автор
Сапкина МР

📅 Дата
2026-02-12
