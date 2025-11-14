# Saucedemo Automation (Java + Selenium + POM)

## Описание
Автоматизация UI-тестов для [saucedemo.com](https://www.saucedemo.com/)  
Используется **Page Object Model**, **Allure**, **TestNG**

## Тесты
1. Успешный логин
2. Неверный пароль
3. Добавление в корзину
4. Удаление из корзины
5. Сортировка по цене

## Стек
- Java 17
- Maven
- Selenium 4.25.0
- TestNG
- Allure
- WebDriverManager

## Запуск
```bash
mvn clean test
allure serve target/allure-results