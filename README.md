# Laboratorio 09 Consumo de API con Retrofit

**Alumno:** Leonel Arenas Arias  
**Correo:** Leonel.arenas@tecsup.edu.pe

Aplicación Android con Kotlin, Jetpack Compose, Retrofit, Gson y arquitectura MVVM.

## Funciones

- Publicaciones de JSONPlaceholder Mock con consulta individual por identificador.
- Ejercicio 1: cinco recetas de DummyJSON con búsqueda, imagen, ingredientes y preparación.
- Reto adicional: catálogo de seis anfibios con imágenes y descripciones.
- Interfaz y contenido de demostración en español.
- Estados de carga, error, reintento y listas vacías.

La aplicación consulta las API por HTTPS. `ContenidoEspanol.kt` contiene traducciones de presentación para los registros del catálogo de demostración; conserva los identificadores, imágenes y datos numéricos de las respuestas. Las recetas se solicitan con `limit=5` para mantener todo el catálogo mostrado en español. No se utiliza un servicio externo de traducción.

## Organización

- `data/ApiRepository.kt`: modelos, interfaces Retrofit y repositorio.
- `data/ContenidoEspanol.kt`: traducciones del contenido.
- `ui/LabViewModel.kt`: peticiones con corrutinas y estado observable.
- `ui/moduloPosts.kt`: pantallas Compose.
- `MainActivity.kt`: tema, Scaffold y navegación.

## Ejecución

Abrir el proyecto en Android Studio, sincronizar Gradle y ejecutar en un dispositivo con Android 7.0 o superior y acceso a Internet.

```powershell
$env:JAVA_HOME='C:\Program Files\Android\Android Studio\jbr'
$env:GRADLE_USER_HOME='C:\Users\Tecsup\.gradle'
.\gradlew.bat assembleDebug testDebugUnitTest
```

## Evidencias

El informe se encuentra en `entrega/Informe-Laboratorio-09-Leonel-Arenas.pdf`. Las capturas y el video del funcionamiento están en `evidencias/`. El APK compilado está en `app/build/outputs/apk/debug/app-debug.apk` y se copia a `entrega/Lab09.apk` para instalarlo.

Las capturas corresponden a la ejecución real en el emulador Medium Phone con API 35. El video recorre publicaciones, detalle, recetas y anfibios.

## Servicios utilizados

- https://json-placeholder.mock.beeceptor.com/posts
- https://dummyjson.com/recipes?limit=5
- https://android-kotlin-fun-mars-server.appspot.com/amphibians

- [Informe con respuestas y capturas](entrega/Informe-Laboratorio-09-Leonel-Arenas.pdf)
- [Video del funcionamiento](evidencias/laboratorio-es.mp4)

