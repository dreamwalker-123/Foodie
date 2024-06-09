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

Заметки:
1. Покрыты unit тестами viewModel из feature:catalog, core:network, core:data
2. Покрыты ui тестами CatalogScreen из feature:catalog, тестирование навигации не вышло из-за сложности тестированияя Lottie анимаций и LaunchedEffect, разберусь позже
3. Шрифты используются стандартные, не везде выдержаны отступы
4. Загрузка с сервера данных начинается только после перехода на CatalogRoute, неплохо было бы доработать, чтобы загрузка начиналась во время Lottie анимации
5. Мелкие недоработки указаны через FIXME:
