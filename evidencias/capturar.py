from pathlib import Path
import subprocess, time, re, xml.etree.ElementTree as ET, sys
sys.stdout.reconfigure(encoding="utf-8")
ROOT = Path(__file__).resolve().parent.parent
ADB = r'C:\Users\Tecsup\AppData\Local\Android\Sdk\platform-tools\adb.exe'
def adb(*args):
 return subprocess.run([ADB, *args], capture_output=True, check=True).stdout

def capture(name):
 adb('shell','screencap','-p','/sdcard/lab.png')
 adb('pull','/sdcard/lab.png',str(ROOT/'evidencias'/name))
 print(name, flush=True)

def tap(x,y):
 adb('shell','input','tap',str(x),str(y)); time.sleep(3)

def tree():
 adb('shell','uiautomator','dump','/sdcard/window.xml')
 return ET.fromstring(adb('shell','cat','/sdcard/window.xml'))

def click_text(text):
 nodes = [n for n in tree().iter('node') if n.get('text') == text]
 if not nodes: raise RuntimeError('No se encontró: '+text)
 coords=list(map(int,re.findall(r'\d+',nodes[0].get('bounds'))))
 tap((coords[0]+coords[2])//2,(coords[1]+coords[3])//2)

if __name__ == '__main__':
 time.sleep(3)
 recorder=subprocess.Popen([ADB,'shell','screenrecord','--size','720x1600','--bit-rate','2500000','--time-limit','100','/sdcard/laboratorio-es.mp4'],stdout=subprocess.DEVNULL,stderr=subprocess.DEVNULL)
 capture('01-inicio.png')
 click_text('Publicaciones'); time.sleep(3)
 for _ in range(4): adb('shell','input','swipe','530','600','530','1900','300')
 time.sleep(2); capture('02-posts.png')
 click_text('Leer publicación →'); time.sleep(3); capture('03-detalle-post.png')
 print('DETALLE:', [n.get('text') for n in tree().iter('node') if n.get('text')], flush=True)
 click_text('Recetas'); time.sleep(4); capture('04-recetas.png')
 click_text('Ver preparación →'); time.sleep(3)
 adb('shell','input','swipe','530','650','530','1850','500'); time.sleep(1)
 capture('05-detalle-receta.png')
 adb('shell','input','swipe','530','1850','530','700','600'); time.sleep(2); capture('06-preparacion-receta.png')
 click_text('Anfibios'); time.sleep(4); capture('07-anfibios.png')
 adb('shell','input','swipe','530','1850','530','700','600'); time.sleep(2); capture('08-anfibios-desplazamiento.png')
 click_text('Inicio'); time.sleep(2)
 adb('shell','pkill','-2','screenrecord')
 recorder.wait(timeout=15)
 adb('pull','/sdcard/laboratorio-es.mp4',str(ROOT/'evidencias'/'laboratorio-es.mp4'))
 print('Video guardado',flush=True)


