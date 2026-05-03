import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

plugins {
    // Android
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library)     apply false
    // Jetbrains
    alias(libs.plugins.jetbrains.kotlin.compose) apply false
}

tasks.register("publishLibraryToMavenLocal") {

    group       = "publishing"
    description = "Publish library to Maven Local."

    val publishTasks = subprojects
        .filter { it.plugins.hasPlugin(libs.plugins.maven.publish.get().pluginId) }
        .map { it.tasks.named("publishToMavenLocal") }
//        .flatMap { project ->
//            project.tasks.matching { task ->
//                task.name.startsWith("publish") && task.name.endsWith("ToMavenLocal")
//            }
//        }

    dependsOn(publishTasks)

    finalizedBy("generateChecksums")

    doLast {
        println("✔ Library published (${rootProject.version})")
    }
}

tasks.register("generateChecksums") {

    group       = "publishing"
    description = "Generate MD5 and SHA1 checksums for all modules before publishing to Maven Local"

    mustRunAfter(tasks.withType<AbstractPublishToMaven>())

    doLast {

        val mavenLocalRepo = File(System.getProperty("user.home"), ".m2/repository")

        val groupPath = rootProject.group.toString().replace(".", "/")

        val ourRepoDir = File(mavenLocalRepo, groupPath)

        if (ourRepoDir.exists()) {

            println("🔍 Searching for artifacts in: ${ourRepoDir.absolutePath}")

            ourRepoDir.walkTopDown()
                .filter { it.isFile }
                .forEach { file ->

                    val isArtifactFile = file.name.matches(Regex(""".*\.(aar|jar|pom|module)$"""))
                    val isChecksumFile = file.name.matches(Regex(""".*\.(md5|sha1|sha256|asc)$"""))

                    if (isArtifactFile && !isChecksumFile) {

                        val md5File = File(file.parent, "${file.name}.md5")
                        val sha1File = File(file.parent, "${file.name}.sha1")

                        if (!md5File.exists() || !sha1File.exists()) {

                            try {

                                val bytes = file.readBytes()

                                if (!md5File.exists()) {

                                    val md5 = MessageDigest.getInstance("MD5")
                                        .digest(bytes)
                                        .joinToString("") { "%02x".format(it) }

                                    md5File.writeText(md5)

                                    println("✓ Generated MD5 for: ${file.relativeTo(mavenLocalRepo)}")
                                }

                                if (!sha1File.exists()) {

                                    val sha1 = MessageDigest.getInstance("SHA-1")
                                        .digest(bytes)
                                        .joinToString("") { "%02x".format(it) }

                                    sha1File.writeText(sha1)

                                    println("✓ Generated SHA1 for: ${file.relativeTo(mavenLocalRepo)}")
                                }

                            } catch (e: IOException) {

                                println("⚠️ IO error generating checksums for ${file.name}: ${e.message}")

                            } catch (e: NoSuchAlgorithmException) {

                                println("⚠️ Algorithm not found when generating checksums for ${file.name}: ${e.message}")

                            }
                        }
                    }
                }
        }
    }
}