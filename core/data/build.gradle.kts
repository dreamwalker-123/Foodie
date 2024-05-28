plugins {
    id("java-library")
    alias(libs.plugins.jetbrainsKotlinJvm)
    alias(libs.plugins.ksp)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
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

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("org.mockito:mockito-core:4.0.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")

    // DI Hilt for Kotlin library
    implementation(libs.hilt.core)
    ksp(libs.hilt.compiler)
}