# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**
-keepclassmembers class com.kemal.goldenhorn.** {
    *** Companion;
}
-keepclasseswithmembers class com.kemal.goldenhorn.** {
    kotlinx.serialization.KSerializer serializer(...);
}
# Ktor
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**
-dontwarn org.slf4j.**
