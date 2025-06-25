import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import io.izzel.taboolib.gradle.*
import io.izzel.taboolib.gradle.Basic
import io.izzel.taboolib.gradle.Bukkit
import io.izzel.taboolib.gradle.BungeeCord
import io.izzel.taboolib.gradle.BukkitHook
import io.izzel.taboolib.gradle.BukkitUI
import io.izzel.taboolib.gradle.BukkitUtil
import io.izzel.taboolib.gradle.BukkitNMS
import io.izzel.taboolib.gradle.BukkitNMSUtil
import io.izzel.taboolib.gradle.BukkitNMSItemTag
import io.izzel.taboolib.gradle.BukkitNMSDataSerializer
import io.izzel.taboolib.gradle.BukkitNMSEntityAI
import io.izzel.taboolib.gradle.BukkitNavigation
import io.izzel.taboolib.gradle.MinecraftChat
import io.izzel.taboolib.gradle.CommandHelper
import io.izzel.taboolib.gradle.MinecraftEffect
import io.izzel.taboolib.gradle.DatabasePlayer
import io.izzel.taboolib.gradle.Database
import io.izzel.taboolib.gradle.Kether


plugins {
    java
    id("io.izzel.taboolib") version "2.0.22"
    id("org.jetbrains.kotlin.jvm") version "1.8.22"
}
java{
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain{
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
taboolib {
    env {
        install(Basic)
        install(Bukkit)
        install(BukkitHook)
        install(BukkitUI)
        install(BukkitUtil)
        install(BukkitNMS)
        install(BukkitNMSUtil)
        install(BukkitNMSItemTag)
        install(BukkitNMSDataSerializer)
        install(BukkitNMSEntityAI)
        install(BukkitNavigation)
        install(MinecraftChat)
        install(CommandHelper)
        install(MinecraftEffect)
        install(DatabasePlayer)
        install(Database)
        install(Kether)
    }
    description {
        name = "MoreAnimals"
        desc("更多动物")
        contributors {
            name("kqn")
            name("stc")
        }
    }
    version { taboolib = "6.2.3" }
}

repositories {
    mavenCentral()
    maven {
        name = "CodeMC"
        url = uri("https://repo.codemc.io/repository/maven-public/")
    }
    maven { url= uri("https://jitpack.io") }
}

dependencies {
    implementation("com.google.firebase:protolite-well-known-types:18.0.1")
    compileOnly("ink.ptms.core:v12004:12004:mapped")
    compileOnly("ink.ptms.core:v12004:12004:universal")

    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
    // https://mvnrepository.com/artifact/org.ow2.asm/asm
    //implementation("org.ow2.asm:asm:9.8")
    // https://mvnrepository.com/artifact/net.bytebuddy/byte-buddy
    implementation("net.bytebuddy:byte-buddy:1.17.5")
    // https://mvnrepository.com/artifact/net.bytebuddy/byte-buddy-agent
    implementation("net.bytebuddy:byte-buddy-agent:1.17.5")
    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.13.1")
    implementation("de.tr7zw:item-nbt-api-plugin:2.15.0")
    // https://mvnrepository.com/artifact/com.github.ben-manes.caffeine/caffeine
    // https://mvnrepository.com/artifact/com.github.ben-manes.caffeine/caffeine
    // https://mvnrepository.com/artifact/com.github.ben-manes.caffeine/caffeine
    // https://mvnrepository.com/artifact/org.ehcache/ehcache
  //  implementation("com.github.kqnan:GlobalMemory:4.0")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
// 定义一个 copyJar 任务，类型为 Copy
val copyJar by tasks.registering(Copy::class) {
    dependsOn(tasks.build) // 确保在 build 任务后执行
    from("build/libs/MoreAnimals-1.0-SNAPSHOT.jar") // 获取构建的 JAR 文件
    into("H:\\Shycraft\\sc\\plugins") // 目标文件夹路径
}
//tasks.get("copyJar").finalizedBy("runBatchFile")
tasks.build.configure {

    this.finalizedBy(copyJar)

}
tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
