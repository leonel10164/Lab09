from pathlib import Path
from shutil import copy2
from docx import Document
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.oxml import OxmlElement
from docx.oxml.ns import qn
from docx.text.paragraph import Paragraph

ROOT=Path(r'C:\Users\Tecsup\AndroidStudioProjects\Lab09')
SRC=Path(r'C:\Users\Tecsup\Downloads\GLAB-S09-BPAREJA-2026-01.docx')
BACKUP=ROOT/'tmp'/'GLAB-S09-original.docx'
if not BACKUP.exists(): copy2(SRC,BACKUP)
d=Document(BACKUP)
paras=list(d.paragraphs)
answers={
1:'El servidor incluye otros recursos como /users (usuarios), /comments (comentarios) y /todos (tareas). Se comprobaron las tres rutas mediante consultas HTTP. Beeceptor también dispone de servidores de ejemplo para empresas y productos, cada uno con su propia URL base.',
5:'La anotación @SerializedName relaciona una propiedad de Kotlin con una clave del JSON al convertir los datos con Gson. Por ejemplo, @SerializedName("comment_count") permite recibir ese valor en la propiedad commentCount. Si los nombres coinciden, como id, la anotación sigue documentando el contrato del servicio.',
7:'@GET indica que Retrofit debe enviar una petición HTTP GET a la ruta especificada. @GET("posts") obtiene el listado. @GET("posts/{id}") consulta un registro; @Path("id") sustituye el identificador en la URL.',
8:'suspend permite suspender y reanudar una función dentro de una corrutina mientras se espera el resultado. La integración de Retrofit realiza la consulta de manera asíncrona y permite esperar su respuesta sin bloquear la interfaz. suspend por sí solo no mueve cualquier operación bloqueante a otro hilo.',
10:'rememberNavController() crea y recuerda un NavHostController dentro de Compose. Mantiene el estado y la pila de navegación entre recomposiciones. El mismo controlador se entrega al NavHost y se usa para navegar con navigate() o regresar con popBackStack().',
14:'Contenido recibe el padding del Scaffold, el controlador de navegación y el servicio. El Box aplica el espacio de las barras para que no cubran la pantalla. El NavHost define el destino inicial y las rutas inicio, posts y postsVer/{id}. La ruta de detalle declara id como entero y lo entrega a ScreenPost. En el proyecto desarrollado, este NavHost está dentro de ProgPrincipal9 y el acceso a los datos se delega al ViewModel.',
16:'LaunchedEffect inicia una corrutina cuando el composable entra en la composición. Si cambia una de sus claves, cancela el bloque anterior y lo vuelve a iniciar; al salir, cancela su corrutina. Se usa LaunchedEffect(Unit) para iniciar la carga del listado y LaunchedEffect(id) al abrir un detalle. En esta solución dispara una acción del ViewModel; la petición pertenece a viewModelScope.',
17:'SnapshotStateList es una lista observable por Compose, creada con mutableStateListOf(). Los cambios en sus elementos, como añadir o eliminar, pueden actualizar los composables que la leen. En la implementación MVVM se utiliza un UiState observable que contiene listas inmutables y se sustituye al recibir cada respuesta.',
18:'LazyColumn muestra una lista vertical desplazable y compone principalmente los elementos necesarios para el área visible. Es adecuada para listas extensas porque no crea todos sus elementos de una vez. Se usa en publicaciones, recetas y anfibios; las publicaciones y recetas tienen claves basadas en su identificador.',
19:'Al pulsar Leer publicación se navega a postsVer/{id}. Navigation extrae el identificador entero. LaunchedEffect(id) inicia la consulta en el ViewModel; el repositorio ejecuta GET posts/{id}. Gson convierte el JSON en PostModel y el ViewModel publica el resultado. Compose muestra título, autor, cuerpo y los campos opcionales de comentarios y enlace. Durante la espera se presenta un indicador de carga y, si falla, un botón para reintentar.',
20:'Sí. Se organizó la solución en tres responsabilidades: la vista (MainActivity.kt y ui/moduloPosts.kt) presenta la información y emite acciones; LabViewModel conserva estados observables y ejecuta corrutinas; ApiRepository contiene los servicios Retrofit y las consultas. Los modelos PostModel, Recipe y Amphibian representan los datos. Así se separan la interfaz y el acceso a la red y se evita configurar Retrofit en cada recomposición.'
}
def format_p(p,size=10):
 p.paragraph_format.space_after=Pt(6)
 p.paragraph_format.space_before=Pt(3)
 p.paragraph_format.line_spacing=1.08
 for run in p.runs:
  run.font.name='Arial'; run.font.size=Pt(size); run.font.color.rgb=RGBColor(0,0,0)

def fill_cell(cell,text):
 cell.text=text
 for p in cell.paragraphs: format_p(p)
 for row in cell._tc.getparent().getparent().findall(qn('w:tr')):
  for h in row.findall('./'+qn('w:trPr')+'/'+qn('w:trHeight')): h.getparent().remove(h)

for i,text in answers.items(): fill_cell(d.tables[i].cell(0,0),text)
fill_cell(d.tables[0].cell(0,1),'Leonel Arenas Arias')

def after(anchor,text='',bold=False):
 el=OxmlElement('w:p'); anchor._p.addnext(el)
 p=Paragraph(el,anchor._parent)
 p.add_run(text).bold=bold; format_p(p)
 return p

def link(p,label,url):
 part=p.part
 rid=part.relate_to(url,'http://schemas.openxmlformats.org/officeDocument/2006/relationships/hyperlink',is_external=True)
 hl=OxmlElement('w:hyperlink'); hl.set(qn('r:id'),rid)
 r=OxmlElement('w:r'); props=OxmlElement('w:rPr'); color=OxmlElement('w:color'); color.set(qn('w:val'),'0563C1'); props.append(color)
 sz=OxmlElement('w:sz'); sz.set(qn('w:val'),'20'); props.append(sz); r.append(props)
 t=OxmlElement('w:t'); t.text=label; r.append(t); hl.append(r); p._p.append(hl)

def screenshots_after(anchor,names,caption):
 p=after(anchor)
 p.alignment=WD_ALIGN_PARAGRAPH.CENTER
 for i,name in enumerate(names):
  if i: p.add_run('   ')
  p.add_run().add_picture(str(ROOT/'evidencias'/name),width=Inches(2.35))
 p.paragraph_format.keep_together=True
 p.paragraph_format.keep_with_next=True
 return after(p,caption)

p=after(paras[35],'Repositorio del laboratorio: ')
link(p,'github.com/leonel10164/Lab09','https://github.com/leonel10164/Lab09')
p=after(p,'Alumno: Leonel Arenas Arias. Correo: Leonel.arenas@tecsup.edu.pe. Fecha: 16 de octubre de 2026.')
after(paras[46],'Implementación realizada: se añadieron Retrofit y converter-gson 2.11.0, Navigation Compose 2.7.7, Lifecycle ViewModel Compose 2.6.1 y Coil Compose 2.6.0. El permiso INTERNET está declarado en el manifiesto.')
after(paras[50],'El modelo se implementó en data/ApiRepository.kt. En el proyecto se corrigió la coma entre body y link y se declararon link y commentCount como opcionales, porque pueden no aparecer en una respuesta.')
after(paras[66],'Adaptación MVVM: Retrofit se configura una sola vez por servicio en ApiRepository. ProgPrincipal9 conserva el NavHostController y obtiene LabViewModel. El repositorio usa la URL de Beeceptor para publicaciones, DummyJSON para recetas y el servidor de Android para anfibios.')
after(paras[83],'El archivo ui/moduloPosts.kt contiene las pantallas. LabViewModel gestiona los estados de carga, datos y error con reintento. Las traducciones locales del catálogo están en data/ContenidoEspanol.kt; las consultas HTTP y las imágenes siguen procediendo de las API.')
p=after(paras[94],'Resultado de ejecución: aplicación compilada con assembleDebug y ejecutada en el emulador Medium Phone con Android API 35. Se visualizaron diez publicaciones y se abrió la publicación 1 por su identificador. testDebugUnitTest finalizó correctamente; la navegación se comprobó en el emulador.')
p=screenshots_after(p,['02-posts.png','03-detalle-post.png'],'Capturas de ejecución: listado de publicaciones y detalle de la publicación 1, en español.')
p=after(p,'Video del funcionamiento: ')
link(p,'Ver recorrido de la aplicación en español','https://github.com/leonel10164/Lab09/blob/main/evidencias/laboratorio-es.mp4')

# Los espacios de respuesta de ejercicio 1 y anfibios son tablas de una celda.
fill_cell(d.tables[21].cell(0,0),'Recurso elegido: recipes de DummyJSON. Se implementó Cocina del mundo con cinco recetas, búsqueda por nombre o cocina, fotografía, valoración, dificultad y tiempo total. Al abrir una receta se muestran porciones, ingredientes y preparación. El catálogo se solicita con GET recipes?limit=5 y el detalle con GET recipes/{id}. Los textos del catálogo se presentan en español mediante traducciones locales. La búsqueda se aplica sobre las cinco recetas descargadas.')
fill_cell(d.tables[22].cell(0,0),'Reto adicional completado: catálogo de seis anfibios mediante GET https://android-kotlin-fun-mars-server.appspot.com/amphibians. Se muestran imagen, nombre, tipo y descripción en español. Se aplicó MVVM con repositorio, ViewModel, corrutinas y estados de carga o error. Coil carga las imágenes y LazyColumn permite recorrer las especies.')

def evidence_cell(cell,names,caption):
 p=cell.add_paragraph(); p.alignment=WD_ALIGN_PARAGRAPH.CENTER
 for i,name in enumerate(names):
  if i: p.add_run('  ')
  p.add_run().add_picture(str(ROOT/'evidencias'/name),width=Inches(2.2))
 p.paragraph_format.keep_together=True
 p=cell.add_paragraph(caption); format_p(p,9)
 p=cell.add_paragraph(); link(p,'Video del funcionamiento en español','https://github.com/leonel10164/Lab09/blob/main/evidencias/laboratorio-es.mp4')
 # Permite dividir la respuesta extensa entre páginas sin recortar su contenido.
 for node in cell._tc.getparent().findall('./'+qn('w:trPr')+'/'+qn('w:cantSplit')): node.getparent().remove(node)

evidence_cell(d.tables[21].cell(0,0),['04-recetas.png','06-preparacion-receta.png'],'Capturas del ejercicio 1: catálogo de recetas y preparación de la pizza margarita.')
evidence_cell(d.tables[22].cell(0,0),['07-anfibios.png','08-anfibios-desplazamiento.png'],'Capturas del reto adicional: catálogo de anfibios y desplazamiento entre especies.')
after(paras[110],'La parte más desafiante del desarrollo fue coordinar la navegación con las consultas de red y el estado de Compose. Las respuestas no llegan de inmediato y pueden fallar; además, una recomposición podría repetir consultas si se ejecutaran directamente al dibujar la pantalla. Se resolvió separando la red en ApiRepository y el estado en LabViewModel. LaunchedEffect inicia la acción de carga y la interfaz presenta un indicador de espera, el resultado o un mensaje con reintento.')
p=after(paras[111],'Se utilizó IA para interpretar la guía, generar y ajustar código Kotlin, proponer la estructura MVVM, traducir el catálogo al español y preparar las respuestas. También se utilizó para compilar, automatizar la navegación del emulador, guardar las capturas reales y grabar el video. Las comprobaciones incluyeron compilación, prueba unitaria inicial y revisión funcional de las pantallas.')
p=after(p,'Fuentes consultadas: documentación de Beeceptor, DummyJSON y Android Developers.')
for label,url in [('Servicios de Beeceptor','https://beeceptor.com/docs/sample-api-for-testing/'),('Recetas de DummyJSON','https://dummyjson.com/docs/recipes'),('Efectos de Compose','https://developer.android.com/develop/ui/compose/side-effects')]:
 p=after(p); link(p,label,url)

# Mantener las preguntas junto al inicio de sus respuestas y evitar alturas exactas.
for idx in answers:
 for row in d.tables[idx].rows:
  row.height=None
  for p in row.cells[0].paragraphs: p.paragraph_format.widow_control=True

OUT=ROOT/'entrega'/'GLAB-S09-BPAREJA-2026-01-completado.docx'
d.save(OUT)
copy2(OUT,SRC)
print(OUT)
