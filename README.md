# Diplom_2

## 📊 Allure отчёт тестирования

Интерактивный отчёт с результатами 11 тестов доступен через GitHub Pages:

👉 **[https://MonikaRS.github.io/Diplom_2/](https://MonikaRS.github.io/Diplom_2/)**

### Результаты тестирования:
- **Всего тестов:** 11
- **Успешно:** 11  
- **Провалено:** 0
- **Время выполнения:** ~26 секунд

### Тестовые наборы:
1. **CreateUserTest** (3 теста) - создание пользователя
2. **LoginUserTest** (2 теста) - авторизация пользователя
3. **CreateOrderTest** (6 тестов) - создание заказа

### Локальный запуск:
```bash
mvn clean test
allure generate target/allure-results -o docs --clean
allure open docs/
