Тестовое задание.

Скриншоты:
1. Lottie Screen
![1](https://github.com/dreamwalker-123/Foodie/assets/125174202/97007ff1-8447-45d0-8955-af8f946fe56b)

2. Catalog Screen
![2](https://github.com/dreamwalker-123/Foodie/assets/125174202/b9f76c7a-806f-4624-9f8d-c41dff3b30af)
![3](https://github.com/dreamwalker-123/Foodie/assets/125174202/2402a663-dde8-409c-9e8e-8f3233acdb64)
![4](https://github.com/dreamwalker-123/Foodie/assets/125174202/7a1761ba-1d9f-465d-9288-a4a9295e6405)
![5](https://github.com/dreamwalker-123/Foodie/assets/125174202/a9967dce-002e-463c-bd88-cb626fdc4281)

3. Card of product Screen
![6](https://github.com/dreamwalker-123/Foodie/assets/125174202/8acd0d4a-e625-495e-b842-54ab5cebb083)

4. Basket Screen
![7](https://github.com/dreamwalker-123/Foodie/assets/125174202/cabf5635-5bc7-42dc-ad84-6da964c16c61)

5. Search Screen
![8](https://github.com/dreamwalker-123/Foodie/assets/125174202/2494a7e1-5090-4c00-a633-7256730c026d)
![9](https://github.com/dreamwalker-123/Foodie/assets/125174202/fb0625c4-9a44-4ca9-8624-5d120d534fec)


Стек:
1. Kotlin
2. Jetpack Compose
3. Jetpack Compose Navigation
4. Coroutines, Flow
5. Retrofit
6. Kotlinx Serialization
7. Lottie Animation
8. Dagger Hilt
9. Clean Architecture
10. MVVM
11. Compose Test, JUnit

Заметки:
1. Покрыты unit тестами viewModel из feature:catalog, core:network, core:data
2. Покрыты ui тестами CatalogScreen из feature:catalog, тестирование навигации не вышло из-за сложности тестированияя Lottie анимаций и LaunchedEffect, разберусь позже
3. Шрифты используются стандартные, не везде выдержаны отступы
4. Загрузка с сервера данных начинается только после перехода на CatalogRoute, неплохо было бы доработать, чтобы загрузка начиналась во время Lottie анимации
5. Другие мелкие недоработки указаны через FIXME:
