repositories {
  mavenCentral()
}
plugins {
  kotlin("jvm") version "1.5.30"
  id("com.github.johnrengelman.shadow") version "6.1.0"
}
dependencies {
  runtimeOnly("org.jetbrains.kotlin:kotlin-scripting-jsr223:1.5.30")
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.12.3")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.3")
  implementation("com.github.sisyphsu:dateparser:1.0.7")
  testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")
  testImplementation("com.natpryce:hamkrest:1.8.0.1")
}
tasks.compileKotlin {
  kotlinOptions.jvmTarget = "16"
  kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.time.ExperimentalTime"
}
tasks.compileTestKotlin {
  kotlinOptions.jvmTarget = "16"
  kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.time.ExperimentalTime"
}
tasks.test {
  useJUnitPlatform()
}
tasks.jar {
  manifest.attributes("Main-Class" to "KqKt")
}
tasks.shadowJar {
  isZip64 = true
}
tasks.build {
  dependsOn(tasks.shadowJar)
}