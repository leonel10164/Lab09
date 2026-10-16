from pathlib import Path
import subprocess,time,re,sys,json,xml.etree.ElementTree as ET
sys.stdout.reconfigure(encoding='utf-8')
R=Path(__file__).resolve().parent.parent
A=r'C:\Users\Tecsup\AppData\Local\Android\Sdk\platform-tools\adb.exe'
def adb(*args): return subprocess.run([A,*args],capture_output=True,check=True).stdout
def tree():
 adb('shell','uiautomator','dump','/sdcard/anfibios.xml')
 return ET.fromstring(adb('shell','cat','/sdcard/anfibios.xml'))
def texts(): return [n.get('text') for n in tree().iter('node') if n.get('text')]
def click(text):
 nodes=[n for n in tree().iter('node') if n.get('text')==text]
 if not nodes: raise AssertionError('Control no encontrado: '+text+' '+str(texts()))
 c=list(map(int,re.findall(r'\d+',nodes[0].get('bounds'))))
 adb('shell','input','tap',str((c[0]+c[2])//2),str((c[1]+c[3])//2));time.sleep(1.5)
def hide_keyboard():
 if 'mInputShown=true' in adb('shell','dumpsys','input_method').decode('utf-8',errors='replace'):
  adb('shell','input','keyevent','4')

def capture(name):
 adb('shell','screencap','-p','/sdcard/anfibios.png');adb('pull','/sdcard/anfibios.png',str(R/'evidencias/anfibios'/name)); print(name,flush=True)
def check(text):
 assert text in texts(), 'Falta: '+text
 print('Verificado: '+text,flush=True)

adb('install','-r',str(R/'app/build/outputs/apk/debug/app-debug.apk'))
adb('shell','am','force-stop','com.example.lab09');adb('shell','am','start','-n','com.example.lab09/.MainActivity');time.sleep(3)
click('Anfibios');time.sleep(3)
rec=subprocess.Popen([A,'shell','screenrecord','--size','720x1600','--bit-rate','2400000','--time-limit','160','/sdcard/reto-anfibios.mp4'],stdout=subprocess.DEVNULL,stderr=subprocess.DEVNULL)
results=[]
try:
 check('6 de 6 especies');capture('01-catalogo.png');results.append('Carga de seis especies desde la API')
 click('Rana');check('2 de 6 especies');capture('02-filtro-ranas.png');results.append('Filtro por tipo: dos ranas')
 click('Buscar especie');adb('shell','input','text','azules');adb('shell','input','keyevent','66');time.sleep(1)
 check('1 de 6 especies');capture('03-busqueda.png');results.append('Búsqueda de azules: una especie')
 click('Ver ficha de la especie →');check('Ficha de la especie');capture('04-detalle.png');results.append('Detalle con imagen, características y hábitat')
 click('← Volver al catálogo');click('Borrar');click('Todos');click('Actualizar');time.sleep(3);check('6 de 6 especies');results.append('Actualización de datos mediante nueva consulta')
 click('Buscar especie');adb('shell','input','text','zzz');adb('shell','input','keyevent','66');time.sleep(1)
 check('No se encontraron especies con esos criterios.');capture('05-sin-resultados.png');results.append('Estado sin coincidencias')
 click('Limpiar filtros');check('6 de 6 especies');capture('06-filtros-limpiados.png');results.append('Restablecimiento del catálogo')
finally:
 try: adb('shell','pkill','-2','screenrecord')
 except subprocess.CalledProcessError: pass
 rec.wait(timeout=15)
 adb('pull','/sdcard/reto-anfibios.mp4',str(R/'evidencias/anfibios/reto-anfibios.mp4'))
 (R/'evidencias/anfibios/verificacion.json').write_text(json.dumps(results,ensure_ascii=False,indent=2),encoding='utf-8')
 print('Video guardado',flush=True)


