// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.detekt) apply true
}

dependencies {
    detekt(libs.detekt.cli)
    detektPlugins(libs.detekt.compose)
    detektPlugins(libs.detekt.formatting)
}

// setting detekt to work over all modules in project
subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    dependencies {
        detektPlugins(rootProject.libs.detekt.compose)
        detektPlugins(rootProject.libs.detekt.formatting)
    }

    detekt {
        val detektAutoCorrect = project.hasProperty("format")
        parallel = true
        buildUponDefaultConfig = false
        config.setFrom(files("${rootProject.projectDir}/app/detekt.yml"))
        baseline = file("${rootProject.projectDir}/app/detekt-baseline.xml")
        autoCorrect = detektAutoCorrect
    }

    // disable detekt on regular build
    tasks.getByPath("detekt").onlyIf { gradle.startParameter.taskNames.any { it.contains("detekt") } }
}
