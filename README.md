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
6. Оформление заказа (шаг 1)
7. Burger menu → Logout
8. Footer, About
9. Проверка ссылок (без переходов)
10. Запуск в Firefox

## Технологии
- Selenium 4.25.0
- TestNG + Maven
- Page Object Model (POM)
- Allure Reports
- WebDriverManager
- Кросс-браузер: Chrome (incognito), Firefox
- Явные ожидания (WebDriverWait)

## Allure Report
[Открыть отчёт](link-to-allure)

## Запуск
```bash
mvn clean test
mvn clean test -Dsuite=testng.xml -Dbrowser=firefox
mvn clean test -Dbrowser=firefox -Dtest=FirefoxSpecificTest
allure serve target/allure-results
