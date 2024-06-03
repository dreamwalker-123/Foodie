plugins {
    id("java-library")
    alias(libs.plugins.jetbrainsKotlinJvm)
    alias(libs.plugins.ksp)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

//test {
//    useJUnitPlatform()
//}

dependencies {
//    api(project(":core:model"))
    implementation(project(":core:network"))
    implementation(project(":core:runtime"))
    implementation(project(":core:model"))

    implementation(libs.kotlinx.coroutines.core)

//    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)

    // DI Hilt for Kotlin library
    implementation(libs.hilt.core)
    ksp(libs.hilt.compiler)
}