const fs = require('fs');
const path = require('path');
const {chromium} = require('C:/Users/Tecsup/.cache/codex-runtimes/codex-primary-runtime/dependencies/node/node_modules/playwright');
const root=path.resolve(__dirname,'../..');
const main='app/src/main/java/com/example/lab09/';
function lines(file,start,end) {const all=fs.readFileSync(path.join(root,file),'utf8').split(/\r?\n/); return all.slice(start-1,end).map((text,i)=>({n:start+i,text}));}
function segment(file,begin,end) {const a=fs.readFileSync(path.join(root,file),'utf8').split(/\r?\n/); let s=a.findIndex(l=>l.includes(begin)); let e=end?a.findIndex((l,i)=>i>s&&l.includes(end)):a.length; return lines(file,s+1,e);}
const api=main+'data/ApiRepository.kt'; const ui=main+'ui/moduloPosts.kt';
const jobs=[
 ['01-permiso-internet','Permiso de acceso a Internet','app/src/main/AndroidManifest.xml',2,16],
 ['02-dependencias','Dependencias del proyecto','app/build.gradle.kts',53,66],
 ['03-modelo-post','Modelo de publicaciones',api,9,11],
 ['04-servicio-post','Interfaz del servicio de publicaciones',api,18,21],
 ['05-retrofit','Configuración de Retrofit y repositorio',api,27,37],
 ['06-navegacion','Scaffold y controlador de navegación',main+'MainActivity.kt',22,34],
 ['07-rutas','Rutas y consulta por identificador',main+'MainActivity.kt',34,44],
 ['08-publicaciones','Presentación del listado de publicaciones',ui,'fun PostsScreen','fun PostScreen'],
 ['09-mvvm','Estado y consultas mediante ViewModel',main+'ui/LabViewModel.kt',9,30],
 ['10-recetas','Listado y búsqueda de recetas',ui,'fun RecipesScreen','fun RecipeScreen'],
 ['11-anfibios','Catálogo de anfibios',ui,'fun AmphibiansScreen',null]
];
const esc=s=>s.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
(async()=>{
 const browser=await chromium.launch({headless:true,channel:"chrome"}); const page=await browser.newPage({viewport:{width:1160,height:900},deviceScaleFactor:1.5});
 const manifest=[];
 for(const [name,title,file,start,end] of jobs){
 const selected=typeof start==='number'?lines(file,start,end):segment(file,start,end);
 const html=`<!DOCTYPE html><html lang="es"><meta charset="utf-8"><style>*{box-sizing:border-box}body{margin:0;background:#151a23;color:#e3e8ef;font-family:Arial}header{padding:22px 30px;background:#202938;border-bottom:1px solid #3b495d}h1{font-size:23px;margin:0 0 8px;color:#fff}header p{margin:0;color:#b0c6e1;font-size:17px;overflow-wrap:anywhere}main{padding:20px 24px 26px}.line{display:flex;align-items:start}.num{flex:none;width:48px;text-align:right;padding-right:16px;color:#899bb0;font:18px/27px Consolas,monospace;user-select:none}pre{margin:0;white-space:pre-wrap;overflow-wrap:anywhere;tab-size:4;font:20px/27px Consolas,monospace;color:#e8edf5;flex:1;min-width:0}.line:nth-child(even){background:#19212c}footer{font-size:15px;color:#b0c6e1;border-top:1px solid #354151;padding:12px 28px}</style><header><h1>${esc(title)}</h1><p>${esc(file)}</p></header><main>${selected.map(l=>`<div class="line"><span class="num">${l.n}</span><pre>${esc(l.text)||' '}</pre></div>`).join('')}</main><footer>Laboratorio 09 · Código del proyecto · Leonel Arenas Arias</footer></html>`;
 fs.writeFileSync(path.join(root,'tmp/codigo',name+'.html'),html);
 await page.setContent(html); await page.locator('body').screenshot({path:path.join(root,'evidencias/codigo',name+'.png')});
 manifest.push({archivo:name+'.png',fuente:file,inicio:selected[0]?.n,fin:selected.at(-1)?.n});
 console.log(name);
 }
 fs.writeFileSync(path.join(root,'evidencias/codigo/indice.json'),JSON.stringify(manifest,null,2)); await browser.close();
})();



