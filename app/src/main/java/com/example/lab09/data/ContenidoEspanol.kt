package com.example.lab09.data

/** Traducciones de presentación del catálogo de demostración. Las peticiones siguen siendo REST. */
object ContenidoEspanol {
 private val posts = listOf(
 "Introducción a la inteligencia artificial" to "Aprende los fundamentos de la inteligencia artificial y sus aplicaciones en distintas industrias.",
 "Desarrollo web con React" to "Crea aplicaciones web modernas con React y explora sus potentes funciones.",
 "Fundamentos de ciencia de datos" to "Descubre el mundo de la ciencia de datos y aprende a analizar e interpretar información.",
 "Algoritmos de aprendizaje automático" to "Explora distintos algoritmos de aprendizaje automático y comprende su funcionamiento.",
 "Introducción a la cadena de bloques" to "Aprende sobre la tecnología de cadena de bloques, sus usos y cómo permite realizar transacciones seguras.",
 "Desarrollo de aplicaciones para iOS" to "Comienza a desarrollar aplicaciones para iOS con Swift y crea aplicaciones con numerosas funciones.",
 "Python para el análisis de datos" to "Utiliza bibliotecas de Python como Pandas y NumPy para manipular y analizar datos.",
 "Buenas prácticas de ciberseguridad" to "Aprende prácticas esenciales de ciberseguridad para proteger tus recursos digitales.",
 "Fundamentos de computación en la nube" to "Comprende los fundamentos de la computación en la nube y sus beneficios para las empresas.",
 "Introducción al diseño de experiencia de usuario" to "Descubre los principios del diseño de experiencia de usuario y crea interfaces intuitivas."
 )
 private val titulosOriginales = listOf("Introduction to Artificial Intelligence", "Web Development with React", "Data Science Fundamentals", "Machine Learning Algorithms", "Introduction to Blockchain", "iOS App Development", "Python for Data Analysis", "Cybersecurity Best Practices", "Cloud Computing Essentials", "Introduction to UX Design")
 fun post(value: PostModel): PostModel = posts.getOrNull(titulosOriginales.indexOf(value.title))?.let { value.copy(title = it.first, body = it.second) } ?: value
 private data class TraduccionReceta(val nombre: String, val cocina: String, val ingredientes: String, val pasos: String)
 private val recetas = listOf(
 TraduccionReceta("Pizza margarita clásica", "Italiana", "Masa para pizza|Salsa de tomate|Queso mozzarella fresco|Hojas de albahaca fresca|Aceite de oliva|Sal y pimienta al gusto", "Precalienta el horno a 245 °C.|Extiende la masa y distribuye la salsa de tomate de manera uniforme.|Añade rodajas de mozzarella y hojas de albahaca fresca.|Rocía con aceite de oliva y sazona con sal y pimienta.|Hornea durante 12 a 15 minutos o hasta que los bordes estén dorados.|Corta en porciones y sirve caliente."),
 TraduccionReceta("Salteado vegetariano", "Asiática", "Tofu en cubos|Ramilletes de brócoli|Zanahorias en rodajas|Pimientos en tiras|Salsa de soja|Jengibre picado|Ajo picado|Aceite de sésamo|Arroz cocido para servir", "Calienta aceite de sésamo en un wok a fuego medio alto.|Añade el jengibre y el ajo picados; saltea hasta que desprendan su aroma.|Añade el tofu y saltéalo hasta que esté dorado.|Incorpora el brócoli, las zanahorias y los pimientos. Cocina hasta que estén tiernos y crujientes.|Añade salsa de soja y mezcla bien.|Sirve sobre arroz cocido."),
 TraduccionReceta("Galletas con chispas de chocolate", "Estadounidense", "Harina de trigo|Mantequilla ablandada|Azúcar morena|Azúcar blanca|Huevos|Extracto de vainilla|Bicarbonato de sodio|Sal|Chispas de chocolate", "Precalienta el horno a 175 °C.|Bate la mantequilla con el azúcar morena y el azúcar blanca.|Incorpora los huevos de uno en uno y añade la vainilla.|Mezcla la harina, el bicarbonato y la sal. Añádelos poco a poco a la mezcla húmeda.|Incorpora las chispas de chocolate.|Coloca cucharadas de masa en bandejas sin engrasar.|Hornea entre 10 y 12 minutos o hasta que los bordes estén dorados.|Deja enfriar unos minutos en la bandeja y luego pasa las galletas a una rejilla."),
 TraduccionReceta("Pasta Alfredo con pollo", "Italiana", "Pasta fetuccini|Pechuga de pollo en tiras|Crema de leche|Queso parmesano rallado|Ajo picado|Mantequilla|Sal y pimienta al gusto|Perejil fresco para decorar", "Cocina los fetuccini según las instrucciones del envase.|Saltea el pollo en mantequilla hasta que esté completamente cocido.|Añade el ajo picado y cocina hasta que desprenda su aroma.|Incorpora la crema y el parmesano. Remueve hasta que el queso se derrita.|Sazona con sal y pimienta al gusto.|Mezcla la salsa Alfredo con la pasta cocida.|Decora con perejil fresco antes de servir."),
 TraduccionReceta("Pollo con salsa de mango", "Mexicana", "Muslos de pollo|Mango en cubos|Cebolla roja finamente picada|Cilantro picado|Jugo de lima|Jalapeño picado|Sal y pimienta al gusto|Arroz cocido para servir", "Sazona los muslos de pollo con sal y pimienta.|Cocina el pollo a la parrilla o al horno hasta que esté completamente cocido.|Mezcla el mango, la cebolla roja, el cilantro, el jalapeño y el jugo de lima.|Corta el pollo cocido en cubos y mézclalo con la salsa de mango.|Sirve sobre arroz cocido.")
 )
 fun recipe(value: Recipe): Recipe = recetas.getOrNull(value.id - 1)?.let { value.copy(name = it.nombre, cuisine = it.cocina, difficulty = if (value.difficulty == "Easy") "Fácil" else "Intermedia", ingredients = it.ingredientes.split("|"), instructions = it.pasos.split("|")) } ?: value
 private val anfibios = mapOf(
 "Great Basin Spadefoot" to Triple("Sapo de espuelas de la Gran Cuenca", "Sapo", "Vive en zonas desérticas y pasa gran parte de su vida bajo tierra. Sus patas traseras tienen una estructura que le ayuda a excavar. Suele ser gris, verde o marrón, con manchas oscuras."),
 "Roraima Bush Toad" to Triple("Sapo del monte Roraima", "Sapo", "Habita en Sudamérica, especialmente en el monte Roraima, entre Venezuela, Brasil y Guyana. Su cuerpo suele ser negro, con manchas o vetas amarillas en la garganta y el vientre."),
 "Pacific Chorus Frog" to Triple("Rana arborícola del Pacífico", "Rana", "Es una rana común en la costa del Pacífico de Norteamérica. Su color varía entre verde y marrón. Se reconoce por una franja marrón que va desde la nariz hasta el oído, pasando por el ojo."),
 "Blue Jeans Frog" to Triple("Rana de patas azules", "Rana", "También se conoce como rana venenosa de fresa. Se distingue por su cuerpo rojo brillante y sus extremidades azuladas o violetas. Sus defensas químicas pueden afectar a algunos depredadores."),
 "California Giant Salamander" to Triple("Salamandra gigante de California", "Salamandra", "Se encuentra en el norte de California y otras zonas del noroeste del Pacífico. Prefiere ambientes templados y muy húmedos. Puede alcanzar unos 30 centímetros de longitud."),
 "Tiger Salamander" to Triple("Salamandra tigre", "Salamandra", "Se encuentra en Norteamérica y recibe su nombre por las manchas amarillentas o verdosas sobre su cuerpo marrón. Prefiere condiciones húmedas, pero pasa poco tiempo en el agua y suele excavar en suelos sueltos.")
 )
 fun amphibian(value: Amphibian): Amphibian = anfibios[value.name]?.let { value.copy(name = it.first, type = it.second, description = it.third) } ?: value
}

