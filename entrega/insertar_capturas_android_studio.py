from pathlib import Path
from zipfile import ZipFile
from lxml import etree
from hashlib import sha256
from shutil import copy2
from PIL import Image
R=Path(r'C:\Users\Tecsup\AndroidStudioProjects\Lab09')
p=Path(r'C:\Users\Tecsup\Downloads\GLAB-S09-BPAREJA-2026-01.docx')
backup=R/'tmp/word-antes-capturas-nativas.docx'
if not backup.exists(): copy2(p,backup)
old_images={sha256(f.read_bytes()).digest():f.name for f in (R/'tmp/codigo-originales').glob('*.png')}
W='http://schemas.openxmlformats.org/wordprocessingml/2006/main'; A='http://schemas.openxmlformats.org/drawingml/2006/main'; WP='http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing'; REL='http://schemas.openxmlformats.org/package/2006/relationships'; RR='http://schemas.openxmlformats.org/officeDocument/2006/relationships'
ns={'w':W,'a':A,'wp':WP,'r':RR}
with ZipFile(backup) as z:
 replacements={}
 for name in z.namelist():
  if name.startswith('word/media/'):
   file=old_images.get(sha256(z.read(name)).digest())
   if file:
    native=R/'evidencias/android-studio'/file
    assert native.exists(),native
    replacements[name]=native.read_bytes()
 xml=etree.fromstring(z.read('word/document.xml'))
 rels=etree.fromstring(z.read('word/_rels/document.xml.rels'))
 targets={x.get('Id'):'word/'+x.get('Target') for x in rels}
 count=0
 for inline in xml.findall('.//wp:inline',ns):
  blip=inline.find('.//a:blip',ns)
  if blip is None: continue
  target=targets.get(blip.get('{'+RR+'}embed'))
  if target not in replacements: continue
  # Capturas originales de la ventana, sin recreación ni alteración de píxeles.
  from io import BytesIO
  width,height=Image.open(BytesIO(replacements[target])).size
  ext=inline.find('wp:extent',ns); cx=int(ext.get('cx')); cy=round(cx*height/width)
  ext.set('cy',str(cy))
  ext2=inline.find('.//a:xfrm/a:ext',ns)
  if ext2 is not None: ext2.set('cy',str(cy))
  count+=1
 for t in xml.findall('.//w:t',ns):
  if t.text and t.text.startswith('Código implementado:'):
   t.text=t.text.replace('Código implementado:','Captura de Android Studio:',1)
 assert count==11,count
 out=R/'entrega/GLAB-S09-BPAREJA-2026-01-completado.docx'
 with ZipFile(out,'w') as result:
  for info in z.infolist():
   value=replacements.get(info.filename,z.read(info.filename))
   if info.filename=='word/document.xml': value=etree.tostring(xml,xml_declaration=True,encoding='UTF-8',standalone=True)
   result.writestr(info,value)
copy2(out,p)
print('Reemplazadas 11 imágenes por capturas reales de Android Studio.')

