Тестовое задание.
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

Скриншоты:
1. Lottie Screen
![1](https://github.com/dreamwalker-123/Foodie/assets/125174202/c4fbc216-ecee-438a-ab15-d4ae8846fa48)


2. Catalog Screen
![2](https://github.com/dreamwalker-123/Foodie/assets/125174202/c9a9845e-f60b-4e18-b0cb-1a6b565df76b)
![3](https://github.com/dreamwalker-123/Foodie/assets/125174202/608566b5-3ba2-48cb-bbf6-fa648b6437a8)
![4](https://github.com/dreamwalker-123/Foodie/assets/125174202/7def761d-bc75-402c-aa98-7ae85a7f547c)
![5](https://github.com/dreamwalker-123/Foodie/assets/125174202/0f40ea79-4991-4108-b252-c662be5509e1)


3. Card of product Screen
![6](https://github.com/dreamwalker-123/Foodie/assets/125174202/e8c40113-c05f-4141-bb8b-16cb23e757b4)


4. Basket Screen
![7](https://github.com/dreamwalker-123/Foodie/assets/125174202/e1fbf4af-2d44-4d67-8702-dfb45c685dae)


5. Search Screen
![8](https://github.com/dreamwalker-123/Foodie/assets/125174202/2971638a-4e0c-41b2-9659-67efd4f66c04)
![9](https://github.com/dreamwalker-123/Foodie/assets/125174202/040ef10f-06a4-43a8-9fe2-e5dd5e46335d)

Заметки:
1. Покрыты unit тестами viewModel из feature:catalog, core:network, core:data
2. Покрыты ui тестами CatalogScreen из feature:catalog, тестирование навигации не вышло из-за сложности тестированияя Lottie анимаций и LaunchedEffect, разберусь позже
3. Шрифты используются стандартные, не везде выдержаны отступы
4. Загрузка с сервера данных начинается только после перехода на CatalogRoute, неплохо было бы доработать, чтобы загрузка начиналась во время Lottie анимации
5. Другие мелкие недоработки указаны через FIXME:
