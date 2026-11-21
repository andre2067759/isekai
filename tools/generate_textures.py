#!/usr/bin/env python3
"""ISEKAI art pipeline: hand-authored, high-detail pixel-art assets.
All artwork is drawn on a 64px canvas with deliberate silhouettes, layered metals,
engraving, emissive cores and material-specific palettes. Requires Pillow.
"""
from pathlib import Path
import json
from PIL import Image, ImageDraw

ROOT = Path("src/main/resources")
ASSET = ROOT / "assets/isekai"
DATA = ROOT / "data/isekai"

PALETTES = {
 "aether": ["#071522","#102d43","#1c526c","#258ca2","#58d4df","#d8ffff","#b9a36a"],
 "moon": ["#090b20","#171b3e","#343d70","#586ba8","#a4c7ed","#f0f6ff","#b7a0e8"],
 "sun": ["#1b100d","#48251a","#87502b","#c1843e","#f1c36b","#fff0c4","#f4df9b"],
 "void": ["#0b0718","#211333","#45245f","#713a91","#b15bd0","#f0c9ff","#9d7bcb"],
 "ember": ["#1d0c0a","#4e1c17","#8e3020","#d0522b","#ff9851","#ffe0a1","#d7a05b"],
 "star": ["#08172c","#18304e","#355a7c","#638eb0","#b4d9f0","#ffffff","#d7c68b"],
}
def family(name):
 n=name.lower()
 for k in ("void","ember","star","moon","sun"):
  if k in n: return k
 return "aether"
def pal(name): return PALETTES[family(name)]
def rgba(hexv): return tuple(bytes.fromhex(hexv.lstrip("#"))) + (255,)
def canvas(size=(64,64), bg=(0,0,0,0)):
 im=Image.new("RGBA",size,bg)
 return im,ImageDraw.Draw(im)
def poly(d, pts, color): d.polygon(pts, fill=rgba(color))
def line(d, pts, color, width=1): d.line(pts, fill=rgba(color), width=width, joint="curve")
def rect(d, box, color): d.rectangle(box, fill=rgba(color))
def gem(d,cx,cy,r,p):
 poly(d,[(cx,cy-r),(cx+r,cy-2),(cx+r//2,cy+r),(cx-r//2,cy+r),(cx-r,cy-2)],p[2])
 poly(d,[(cx,cy-r+3),(cx+r//2,cy-2),(cx,cy+r-3),(cx-r//2,cy-2)],p[4])
 line(d,[(cx,cy-r+3),(cx,cy+r-3)],p[5],1)
def metal_edge(d,pts,p):
 poly(d,pts,p[0]); line(d,pts+[pts[0]],p[4],2)
def sword_art(name):
 p=pal(name); im,d=canvas()
 # dark engraved silhouette and bevelled blade; each family receives a distinct profile
 if "greatblade" in name:
  blade=[(31,3),(39,12),(37,29),(33,39),(29,32),(25,13)]
 elif "dagger" in name:
  blade=[(31,5),(38,17),(34,30),(30,34),(25,20)]
 elif "spear" in name:
  blade=[(31,2),(40,15),(34,25),(31,29),(27,23),(23,15)]
 else:
  blade=[(31,3),(38,13),(35,29),(31,38),(27,29),(24,13)]
 metal_edge(d,blade,p)
 poly(d,[(31,6),(35,14),(33,28),(31,34),(29,27),(28,14)],p[3])
 poly(d,[(31,7),(33,14),(32,25),(31,30),(30,17)],p[5])
 line(d,[(31,10),(31,30)],p[4],1)
 # fuller / arcane engraving
 line(d,[(28,18),(31,22),(34,18)],p[6],1)
 if family(name) in ("void","moon","star"):
  gem(d,31,14,4,p)
 else:
  poly(d,[(28,20),(31,24),(34,20),(31,27)],p[6])
 # crossguard, grip, pommel, fine gold fittings
 poly(d,[(17,34),(27,32),(31,35),(35,32),(46,35),(43,39),(32,38),(21,40)],p[0])
 line(d,[(18,35),(28,34),(31,36),(35,34),(45,36)],p[5],2)
 poly(d,[(29,38),(34,38),(35,54),(32,59),(28,54)],p[1])
 line(d,[(30,40),(33,54)],p[4],1)
 for y in (42,48,53): line(d,[(29,y),(34,y-1)],p[6],1)
 poly(d,[(28,56),(32,59),(36,56),(33,62),(30,62)],p[6])
 # spark accents
 d.point((23,12),fill=rgba(p[5])); d.point((40,23),fill=rgba(p[4]))
 return im
def bow_art(name):
 p=pal(name); im,d=canvas()
 # sweeping recurved limbs with layered inlays
 line(d,[(18,5),(11,12),(9,24),(13,39),(23,55)],p[0],7)
 line(d,[(18,5),(13,13),(12,24),(16,39),(24,54)],p[3],3)
 line(d,[(18,5),(13,13),(12,24),(16,39),(24,54)],p[5],1)
 line(d,[(18,5),(31,31),(24,59)],p[5],1)
 line(d,[(31,31),(47,31)],p[6],2)
 gem(d,31,31,5,p)
 for x,y in [(14,17),(14,39),(20,51)]: line(d,[(x-2,y),(x+3,y-4)],p[6],1)
 return im
def armor_icon(name):
 p=pal(name); im,d=canvas()
 if "helmet" in name:
  poly(d,[(12,49),(10,24),(17,10),(31,5),(46,12),(53,28),(49,49),(40,55),(23,55)],p[0])
  poly(d,[(16,44),(15,25),(20,14),(31,10),(43,16),(48,29),(44,44),(37,49),(25,49)],p[2])
  poly(d,[(20,26),(24,17),(31,14),(39,18),(44,28),(40,32),(23,32)],p[3])
  poly(d,[(17,35),(27,34),(31,40),(35,34),(46,35),(42,45),(31,51),(20,45)],p[1])
  line(d,[(17,35),(27,34),(31,40),(35,34),(46,35)],p[5],2)
  gem(d,31,25,5,p)
  line(d,[(31,8),(31,16)],p[6],2)
 elif "chestplate" in name:
  poly(d,[(8,15),(20,9),(28,16),(35,16),(44,9),(56,15),(52,31),(47,57),(16,57),(11,31)],p[0])
  poly(d,[(13,18),(21,14),(29,21),(35,21),(43,14),(51,18),(47,31),(43,52),(20,52),(16,31)],p[2])
  poly(d,[(17,19),(24,16),(30,23),(34,23),(40,16),(47,19),(44,28),(37,34),(27,34),(20,28)],p[3])
  line(d,[(31,20),(31,51)],p[5],2)
  line(d,[(17,31),(25,36),(31,35),(37,36),(46,31)],p[6],2)
  gem(d,31,29,6,p)
  for x in (14,48): line(d,[(x,23),(x+3,29),(x+2,38)],p[5],1)
 elif "leggings" in name:
  poly(d,[(12,8),(51,8),(48,29),(43,56),(34,57),(31,34),(28,57),(19,56),(14,29)],p[0])
  poly(d,[(16,12),(47,12),(44,29),(40,52),(36,52),(32,30),(28,52),(23,52),(19,29)],p[2])
  line(d,[(16,18),(47,18)],p[5],2);line(d,[(31,19),(31,49)],p[6],2)
  for y in (27,36,45): line(d,[(20,y),(27,y+2)],p[3],2);line(d,[(35,y+2),(42,y)],p[3],2)
 else:
  poly(d,[(15,8),(30,8),(30,40),(48,43),(53,52),(49,57),(9,57),(8,51),(18,41)],p[0])
  poly(d,[(19,12),(26,12),(26,39),(45,46),(47,51),(14,51),(14,48),(22,40)],p[2])
  line(d,[(18,43),(44,49)],p[5],2);gem(d,28,27,4,p)
 return im
def staff_art(name):
 p=pal(name); im,d=canvas()
 # ornate mage's sceptre: carved darkwood shaft, silver collars and a floating prism
 line(d,[(31,20),(31,60)],p[0],8);line(d,[(31,22),(31,57)],p[2],4)
 line(d,[(29,28),(33,28)],p[5],2);line(d,[(29,46),(33,46)],p[6],2)
 poly(d,[(31,2),(39,10),(37,19),(31,24),(25,19),(23,10)],p[0])
 poly(d,[(31,5),(36,11),(34,17),(31,20),(28,17),(26,11)],p[3])
 gem(d,31,12,7,p)
 line(d,[(22,8),(17,4)],p[5],1);line(d,[(40,8),(45,4)],p[5],1)
 poly(d,[(26,56),(31,61),(36,56),(34,63),(28,63)],p[6])
 return im
def block_art(name):
 p=pal(name); im,d=canvas()
 # seamless mineral/stone tile with restrained noise and a readable ore cluster
 rect(d,(0,0,63,63),p[0])
 for y in range(2,62,4):
  for x in range(2,62,4):
   v=((x*13+y*17+x*y)%11)
   if v<3: rect(d,(x,y,x+1,y+1),p[1])
   elif v<6: rect(d,(x,y,x+1,y+1),p[2])
 poly(d,[(7,15),(20,7),(34,12),(29,25),(15,29)],p[1])
 poly(d,[(37,35),(50,28),(58,40),(51,55),(38,51)],p[1])
 for x,y in [(15,17),(22,42),(45,42),(51,47)]:
  poly(d,[(x,y-6),(x+5,y-2),(x+4,y+4),(x,y+6),(x-4,y+2)],p[3])
  poly(d,[(x,y-4),(x+3,y-1),(x,y+2),(x-2,y)],p[5])
 line(d,[(3,3),(60,3)],p[2],1);line(d,[(3,60),(60,60)],p[0],1)
 return im
def tool_art(name):
 p=pal(name); im,d=canvas()
 if "pickaxe" in name:
  line(d,[(30,20),(31,59)],p[0],7);line(d,[(30,20),(31,58)],p[3],3)
  poly(d,[(7,19),(12,9),(25,5),(43,8),(56,17),(51,24),(34,20),(18,23),(8,27)],p[0])
  line(d,[(10,18),(24,11),(43,13),(53,19)],p[5],2);gem(d,31,17,4,p)
 elif "axe" in name:
  line(d,[(31,19),(31,60)],p[0],7);line(d,[(31,20),(31,57)],p[3],3)
  poly(d,[(29,8),(44,6),(55,14),(54,29),(43,36),(31,29),(24,20)],p[0])
  poly(d,[(31,12),(43,10),(49,16),(47,26),(39,29),(32,24)],p[3]);line(d,[(34,13),(44,12)],p[5],2)
 else:
  line(d,[(31,23),(31,60)],p[0],6);line(d,[(31,23),(31,58)],p[3],2)
  poly(d,[(22,7),(40,7),(39,21),(34,28),(28,28),(23,21)],p[0]);poly(d,[(25,10),(37,10),(36,20),(31,24),(27,20)],p[3])
 return im
def material_art(name):
 p=pal(name); im,d=canvas()
 if "ingot" in name or "steel" in name:
  poly(d,[(7,23),(15,12),(49,12),(57,23),(51,48),(13,48)],p[0])
  poly(d,[(11,23),(18,16),(46,16),(52,23),(47,43),(16,43)],p[2])
  poly(d,[(17,18),(46,18),(49,23),(20,24)],p[5])
  line(d,[(13,38),(48,38)],p[1],2);line(d,[(19,27),(44,26)],p[4],1)
  for x in (19,31,43): d.point((x,33),fill=rgba(p[6]))
 else:
  poly(d,[(31,3),(49,17),(45,43),(31,60),(14,44),(12,18)],p[0])
  poly(d,[(31,7),(44,19),(41,41),(31,54),(19,41),(17,20)],p[2])
  poly(d,[(31,7),(44,19),(31,29),(17,20)],p[4])
  poly(d,[(31,29),(41,22),(38,41),(31,51)],p[3])
  poly(d,[(31,29),(17,20),(21,41),(31,51)],p[1])
  line(d,[(31,7),(31,52)],p[5],1)
  for x,y in [(10,13),(51,31),(18,54)]: d.point((x,y),fill=rgba(p[5]))
 return im
def relic_art(name):
 p=pal(name); im,d=canvas()
 if "ring" in name:
  d.ellipse((10,12,54,56),outline=rgba(p[0]),width=8);d.ellipse((15,17,49,51),outline=rgba(p[4]),width=3)
  poly(d,[(22,14),(31,4),(40,14),(36,24),(27,24)],p[6]);gem(d,31,15,7,p)
  line(d,[(16,37),(21,47),(31,51),(42,46)],p[5],2)
 else:
  line(d,[(31,2),(31,13)],p[6],3)
  poly(d,[(31,9),(50,21),(46,45),(31,59),(16,45),(12,21)],p[0])
  poly(d,[(31,14),(44,24),(41,42),(31,52),(21,42),(18,24)],p[3])
  poly(d,[(31,18),(39,27),(31,42),(23,27)],p[1])
  gem(d,31,28,8,p)
  line(d,[(18,23),(31,15),(44,23)],p[5],2)
 return im
def food_art(name):
 p=pal(name); im,d=canvas()
 poly(d,[(31,12),(44,9),(55,22),(50,43),(34,57),(16,50),(8,31),(16,15)],p[0])
 poly(d,[(31,16),(43,14),(50,25),(45,42),(33,52),(19,45),(13,30),(19,19)],p[3])
 line(d,[(31,17),(34,6),(45,3)],p[6],4);line(d,[(32,18),(34,7)],p[5],1)
 d.ellipse((20,24,26,31),fill=rgba(p[5]));d.ellipse((38,33,43,39),fill=rgba(p[4]))
 return im
def generic_item(name):
 if "staff" in name: return staff_art(name)\n if any(x in name for x in ("sword","greatblade","dagger","spear")): return sword_art(name)
 if "bow" in name: return bow_art(name)
 if any(x in name for x in ("helmet","chestplate","leggings","boots")): return armor_icon(name)
 if any(x in name for x in ("pickaxe","axe","shovel","hoe")): return tool_art(name)
 if any(x in name for x in ("ingot","steel","crystal","shard","dust","ore","raw_aether")): return material_art(name)
 if any(x in name for x in ("amulet","talisman","ring")): return relic_art(name)
 if "fruit" in name: return food_art(name)
 p=pal(name);im,d=canvas();gem(d,32,31,22,p);return im
def armor_atlas(name):
 """64x32 UV atlas with material-specific plate, seam, trim and emissive motifs."""
 p=pal(name);im=Image.new("RGBA",(64,32),(0,0,0,0));d=ImageDraw.Draw(im)
 # UV islands are kept separate by transparent gutters; each island has a bevelled plate.
 islands=[(0,0,8,8),(8,0,16,8),(16,0,24,8),(24,0,32,8),
          (0,8,8,16),(8,8,16,16),(16,8,24,16),(24,8,32,16),
          (32,0,40,8),(40,0,48,8),(48,0,56,8),(56,0,64,8),
          (32,8,40,16),(40,8,48,16),(48,8,56,16),(56,8,64,16),
          (0,16,8,24),(8,16,16,24),(16,16,24,24),(24,16,32,24),
          (32,16,40,24),(40,16,48,24),(48,16,56,24),(56,16,64,24)]
 for i,(x0,y0,x1,y1) in enumerate(islands):
  # varied plate values make UV faces read as one crafted suit, not tiled squares
  base=p[1] if i%3==0 else p[2]
  d.rectangle((x0,y0,x1-1,y1-1),fill=rgba(base))
  d.line((x0,y0,x1-1,y0),fill=rgba(p[4]),width=1)
  d.line((x0,y0,x0,y1-1),fill=rgba(p[3]),width=1)
  d.line((x0,y1-1,x1-1,y1-1),fill=rgba(p[0]),width=1)
  if i%4==0:
   d.line((x0+2,y0+2,x1-3,y1-3),fill=rgba(p[5]),width=1)
  elif i%4==1:
   d.line((x0+2,y1-3,x1-3,y0+2),fill=rgba(p[6]),width=1)
  else:
   d.point((x0+4,y0+4),fill=rgba(p[5]))
 # Fine energy conduits along the lower atlas edge
 for x in range(2,63,8):
  d.line((x,27,x+3,29),fill=rgba(p[4]),width=1)
  d.point((x+4,28),fill=rgba(p[5]))
 return im
def entity_art(name):
 p=pal(name);im,d=canvas((64,64))
 # sentinel: layered obsidian shell, cyan eyes, segmented limbs, core and etched plates
 poly(d,[(20,5),(28,1),(38,4),(45,13),(42,23),(22,23),(16,14)],p[0])
 poly(d,[(22,7),(29,4),(37,7),(41,14),(36,20),(24,20),(20,14)],p[2])
 poly(d,[(22,12),(29,10),(35,12),(33,17),(25,17)],p[1])
 rect(d,(23,12,28,14),p[5]);rect(d,(35,12,40,14),p[5])
 poly(d,[(15,25),(27,21),(39,23),(49,34),(45,48),(37,54),(20,52),(11,41)],p[0])
 poly(d,[(19,27),(28,24),(38,26),(44,36),(40,46),(35,49),(22,47),(16,39)],p[2])
 poly(d,[(28,27),(38,30),(35,42),(29,46),(24,39)],p[4]);gem(d,31,35,5,p)
 for pts in [[(17,28),(8,33),(7,44),(16,49)],[(44,28),(55,33),(56,44),(47,49)]]:
  line(d,pts,p[0],6);line(d,pts,p[3],2)
 poly(d,[(20,49),(29,51),(27,61),(20,63),(16,57)],p[0])
 poly(d,[(36,49),(44,48),(49,58),(43,63),(36,60)],p[0])
 for x,y in [(17,31),(47,31),(20,42),(42,42),(23,53),(40,53)]: d.point((x,y),fill=rgba(p[4]))
 return im
def shaped(pattern,key,result):
 return {"type":"minecraft:crafting_shaped","pattern":pattern,"key":key,"result":{"item":"isekai:"+result,"count":1}}
NEW={
 "voidsteel_ingot":"item","embersteel_ingot":"item","starlight_ingot":"item","void_crystal":"item","ember_crystal":"item","starlight_crystal":"item",
 "void_sword":"handheld","sun_greatblade":"handheld","moon_dagger":"handheld","aether_spear":"handheld","aether_staff":"handheld",
 "void_amulet":"item","sun_ring":"item","moon_talisman":"item","greater_aether_fruit":"item"}
REC={
 "voidsteel_ingot":shaped(["SSS","SCS","SSS"],{"S":{"item":"isekai:aether_shard"},"C":{"item":"isekai:moon_crystal"}},"voidsteel_ingot"),
 "embersteel_ingot":shaped(["SSS","SCS","SSS"],{"S":{"item":"isekai:aether_shard"},"C":{"item":"isekai:sun_crystal"}},"embersteel_ingot"),
 "starlight_ingot":shaped(["SSS","SCS","SSS"],{"S":{"item":"isekai:aether_shard"},"C":{"item":"isekai:aether_crystal"}},"starlight_ingot"),
 "void_crystal":shaped([" S ","SCS"," S "],{"S":{"item":"isekai:aether_shard"},"C":{"item":"isekai:moon_crystal"}},"void_crystal"),
 "ember_crystal":shaped([" S ","SCS"," S "],{"S":{"item":"isekai:aether_shard"},"C":{"item":"isekai:sun_crystal"}},"ember_crystal"),
 "starlight_crystal":shaped([" S ","SCS"," S "],{"S":{"item":"isekai:aether_shard"},"C":{"item":"isekai:aether_crystal"}},"starlight_crystal"),
 "void_sword":shaped(["V","V","S"],{"V":{"item":"isekai:voidsteel_ingot"},"S":{"item":"minecraft:stick"}},"void_sword"),
 "sun_greatblade":shaped(["E","E","S"],{"E":{"item":"isekai:embersteel_ingot"},"S":{"item":"minecraft:stick"}},"sun_greatblade"),
 "moon_dagger":shaped(["V","S"],{"V":{"item":"isekai:voidsteel_ingot"},"S":{"item":"minecraft:stick"}},"moon_dagger"),
 "aether_spear":shaped([" A "," A "," S "],{"A":{"item":"isekai:aether_ingot"},"S":{"item":"minecraft:stick"}},"aether_spear"),
 "aether_staff":shaped([" C "," A "," S "],{"C":{"item":"isekai:aether_crystal"},"A":{"item":"isekai:aether_ingot"},"S":{"item":"minecraft:stick"}},"aether_staff"),
 "void_amulet":shaped([" V ","VCV"," V "],{"V":{"item":"isekai:voidsteel_ingot"},"C":{"item":"isekai:void_crystal"}},"void_amulet"),
 "sun_ring":shaped([" E ","ESE"," E "],{"E":{"item":"isekai:embersteel_ingot"},"S":{"item":"isekai:ember_crystal"}},"sun_ring"),
 "moon_talisman":shaped([" V ","VCV"," V "],{"V":{"item":"isekai:voidsteel_ingot"},"C":{"item":"isekai:starlight_crystal"}},"moon_talisman"),
 "greater_aether_fruit":shaped([" C ","FAF"," C "],{"C":{"item":"isekai:aether_crystal"},"F":{"item":"isekai:aether_fruit"},"A":{"item":"minecraft:golden_apple"}},"greater_aether_fruit")}
def save(im,path):
 path.parent.mkdir(parents=True,exist_ok=True)
 # nearest-neighbour upscale preserves crisp pixel edges while giving 128px source detail
 if im.size==(64,64): im=im.resize((128,128),Image.Resampling.NEAREST)
 im.save(path,optimize=True)
def main():
 texture_root=ASSET/"textures"
 for path in texture_root.rglob("*.png"):
  n=path.stem.lower()
  if "models/armor" in path.as_posix():
   image=armor_atlas(n)
  elif "textures/entity" in path.as_posix():
   image=entity_art(n)
  elif "/block/" in path.as_posix():
   image=generic_item(n)
  else:
   image=generic_item(n)
  save(image,path)
 for n,kind in NEW.items():
  save(generic_item(n),ASSET/"textures/item"/(n+".png"))
  model={"parent":"minecraft:item/handheld" if kind=="handheld" else "minecraft:item/generated","textures":{"layer0":"isekai:item/"+n}}
  (ASSET/"models/item"/(n+".json")).parent.mkdir(parents=True,exist_ok=True)
  (ASSET/"models/item"/(n+".json")).write_text(json.dumps(model,indent=2)+"\n")
  (DATA/"recipes"/(n+".json")).parent.mkdir(parents=True,exist_ok=True)
  (DATA/"recipes"/(n+".json")).write_text(json.dumps(REC[n],indent=2)+"\n")
 save(generic_item("aether_crystal"),ASSET/"icon.png")
 print("ISEKAI art pipeline: regenerated",len(list(texture_root.rglob("*.png"))),"existing textures and",len(NEW),"new item assets")
if __name__=="__main__": main()
