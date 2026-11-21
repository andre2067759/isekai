#!/usr/bin/env python3
"""ISEKAI pixel-art forge: deterministic 32px item art, block art and generated content."""
from pathlib import Path
import json,struct,zlib
R=Path("src/main/resources")
A=R/"assets/isekai"
D=R/"data/isekai"
P={
"aether":[(5,12,25,255),(12,35,58,255),(19,83,111,255),(28,151,173,255),(100,224,226,255),(225,255,255,255)],
"moon":[(10,9,28,255),(28,25,63,255),(58,54,117,255),(103,94,185,255),(172,160,239,255),(244,238,255,255)],
"sun":[(28,13,7,255),(76,35,13,255),(135,67,20,255),(195,119,34,255),(245,184,69,255),(255,239,187,255)],
"void":[(7,5,17,255),(26,17,45,255),(61,31,91,255),(112,49,157,255),(185,92,226,255),(245,205,255,255)],
"ember":[(30,8,5,255),(86,22,12,255),(154,44,17,255),(217,83,25,255),(255,151,51,255),(255,226,159,255)],
"star":[(8,17,35,255),(26,47,78,255),(56,91,134,255),(108,155,194,255),(190,221,244,255),(255,255,255,255)]}
def palette(n):
 k=next((x for x in ("void","ember","star","moon","sun") if x in n.lower()),"aether");return P[k]
class C:
 def __init__(self,w,h):self.w,self.h=w,h;self.p=[(0,0,0,0)]*(w*h)
 def put(self,x,y,c):
  if 0<=x<self.w and 0<=y<self.h:self.p[y*self.w+x]=c
 def rect(self,x,y,w,h,c):
  for j in range(y,y+h):
   for i in range(x,x+w):self.put(i,j,c)
 def poly(self,pts,c):
  for y in range(min(q[1] for q in pts),max(q[1] for q in pts)+1):
   xs=[]
   for i,(x1,y1) in enumerate(pts):
    x2,y2=pts[(i+1)%len(pts)]
    if (y1<=y<y2) or (y2<=y<y1):xs.append(x1+(y-y1)*(x2-x1)/(y2-y1))
   xs.sort()
   for i in range(0,len(xs)-1,2):
    for x in range(int(xs[i]+.999),int(xs[i+1])+1):self.put(x,y,c)
 def line(self,x0,y0,x1,y1,c,t=1):
  dx,sx=abs(x1-x0),1 if x0<x1 else -1;dy,sy=-abs(y1-y0),1 if y0<y1 else -1;e=dx+dy
  while True:
   for j in range(-t+1,t):
    for i in range(-t+1,t):
     if abs(i)+abs(j)<t+1:self.put(x0+i,y0+j,c)
   if x0==x1 and y0==y1:break
   q=2*e
   if q>=dy:e+=dy;x0+=sx
   if q<=dx:e+=dx;y0+=sy
 def save(self,path):
  raw=bytearray()
  for y in range(self.h):
   raw.append(0)
   for x in range(self.w):raw.extend(self.p[y*self.w+x])
  def ch(k,d):return struct.pack(">I",len(d))+k+d+struct.pack(">I",zlib.crc32(k+d)&0xffffffff)
  b=b"\x89PNG\r\n\x1a\n"+ch(b"IHDR",struct.pack(">IIBBBBB",self.w,self.h,8,6,0,0,0))+ch(b"IDAT",zlib.compress(bytes(raw),9))+ch(b"IEND",b"")
  path.parent.mkdir(parents=True,exist_ok=True);path.write_bytes(b)
def item(n):
 c=C(32,32);p=palette(n);d,m,g,h,w,b=p;name=n.lower()
 def blade():
  c.poly([(15,2),(23,11),(19,23),(16,28),(11,22),(9,12)],d)
  c.poly([(15,4),(20,12),(17,22),(15,25),(13,21),(12,12)],m)
  c.line(14,7,16,22,b,2)
 if "greatblade" in name:
  blade();c.poly([(7,21),(15,24),(25,20),(22,25),(16,28),(9,25)],g);c.rect(14,26,4,4,d);c.rect(11,29,10,2,h)
 elif "dagger" in name:
  c.poly([(16,3),(21,12),(18,21),(14,21),(11,12)],d);c.poly([(16,5),(19,12),(16,18),(14,12)],b);c.poly([(9,19),(16,22),(23,19),(20,23),(16,25),(12,23)],g);c.rect(14,24,4,6,d)
 elif "spear" in name or "staff" in name:
  c.line(16,8,16,30,d,4);c.line(16,10,16,29,m,2);c.poly([(16,1),(23,9),(16,15),(9,9)],d);c.poly([(16,3),(20,9),(16,12),(12,9)],b);c.line(10,18,22,18,g,2)
 elif "sword" in name:
  blade();c.poly([(7,20),(15,23),(25,20),(22,24),(15,27),(9,24)],g);c.rect(13,26,4,4,d);c.rect(11,29,8,2,h);c.put(17,8,b)
 elif "bow" in name:
  c.poly([(7,2),(15,6),(18,14),(15,24),(7,30),(10,22),(13,14),(10,7)],d);c.line(10,3,16,14,b,2);c.line(16,14,10,29,w,2);c.line(16,14,28,14,h);c.put(16,14,b)
 elif "helmet" in name:
  c.poly([(5,22),(5,10),(11,4),(21,4),(27,11),(26,22),(20,26),(9,25)],d);c.poly([(8,19),(9,11),(12,7),(20,7),(24,12),(23,19),(18,22),(11,22)],m);c.rect(10,16,13,3,g);c.put(16,11,b)
 elif "chestplate" in name:
  c.poly([(5,5),(11,8),(16,12),(21,8),(27,5),(30,12),(25,28),(7,28),(2,12)],d);c.poly([(8,8),(13,11),(16,15),(20,11),(24,8),(26,13),(22,25),(10,25),(6,13)],m);c.line(16,14,16,25,b,2);c.put(16,18,w)
 elif "leggings" in name:
  c.poly([(6,4),(26,4),(24,15),(21,29),(16,29),(15,17),(11,29),(6,29),(8,15)],d);c.rect(8,7,16,7,m);c.line(16,8,16,27,b,2)
 elif "boots" in name:
  c.poly([(7,4),(15,4),(15,22),(26,24),(28,29),(4,29),(4,25),(8,22)],d);c.poly([(10,7),(13,7),(13,23),(24,26),(24,27),(7,27),(10,23)],m);c.line(8,24,23,26,b,2)
 elif "ingot" in name or "steel" in name:
  c.poly([(3,12),(8,6),(25,6),(29,12),(25,25),(6,25)],d);c.poly([(6,12),(10,9),(23,9),(26,13),(23,22),(8,22)],m);c.line(9,10,22,10,b,2);c.line(7,19,23,19,g)
 elif "crystal" in name or "shard" in name or "dust" in name or "raw_aether" in name:
  c.poly([(16,2),(25,10),(23,23),(16,30),(7,23),(5,11)],d);c.poly([(16,4),(22,11),(20,22),(16,26),(10,21),(9,12)],g);c.poly([(16,4),(22,11),(16,15)],b);c.line(16,6,16,25,w,2)
 elif "amulet" in name or "talisman" in name:
  c.poly([(16,3),(26,12),(23,25),(16,29),(9,25),(6,12)],d);c.poly([(16,7),(22,13),(20,22),(16,25),(12,22),(10,13)],g);c.poly([(16,10),(19,15),(16,21),(13,15)],b);c.line(16,1,16,5,w,2)
 elif "ring" in name:
  c.poly([(16,4),(25,8),(28,17),(22,27),(10,27),(4,17),(7,8)],d);c.poly([(16,8),(22,11),(24,17),(20,23),(12,23),(8,17),(10,11)],b);c.poly([(16,12),(20,15),(19,20),(13,20),(12,15)],g)
 elif "fruit" in name:
  c.poly([(16,7),(23,5),(28,13),(25,24),(17,29),(7,24),(4,14),(9,7)],d);c.poly([(16,9),(22,8),(25,14),(22,23),(16,26),(9,22),(7,15),(11,10)],g);c.line(16,8,20,2,h,3);c.put(11,13,b)
 else:
  c.poly([(7,7),(16,3),(26,8),(28,21),(17,29),(5,22)],d);c.poly([(9,9),(16,6),(23,10),(24,20),(17,26),(8,20)],m);c.poly([(16,9),(21,15),(16,22),(11,15)],b)
 c.put(4,5,b);c.put(27,4,w);return c
def block(path):
 c=C(32,32);p=palette(path.name);c.rect(0,0,32,32,p[0])
 for y in range(2,30):
  for x in range(2,30):
   q=(x*13+y*17+x*y)%19;c.put(x,y,p[1] if q<5 else p[2] if q<12 else p[0])
 if "ore" in path.name:
  for x,y in [(6,7),(19,5),(13,16),(24,23),(7,25),(21,14)]:
   c.poly([(x,y-4),(x+4,y-1),(x+3,y+3),(x,y+5),(x-3,y+2)],p[3]);c.poly([(x,y-2),(x+2,y),(x,y+2),(x-1,y)],p[5])
 else:
  c.poly([(5,8),(13,3),(25,8),(22,19),(13,25),(5,19)],p[2]);c.line(7,9,13,5,p[4],2);c.line(13,5,23,9,p[3],2)
 for i in range(32):c.put(i,0,p[0]);c.put(i,31,p[0]);c.put(0,i,p[0]);c.put(31,i,p[0])
 return c
def armor(n):
 c=C(64,32);p=palette(n)
 for x,y in [(0,0),(16,0),(32,0),(48,0),(0,16),(16,16),(32,16),(48,16)]:
  c.rect(x+1,y+1,14,14,p[1]);c.rect(x+2,y+2,12,12,p[2]);c.line(x+3,y+3,x+12,y+3,p[4],2);c.line(x+3,y+12,x+12,y+12,p[0],2);c.line(x+3,y+5,x+12,y+10,p[3]);c.put(x+8,y+8,p[5])
 return c
def sentinel():
 c=C(64,64);p=P["aether"];c.rect(0,0,64,64,(7,12,25,255))
 c.poly([(20,5),(28,1),(38,5),(44,14),(39,23),(25,23),(17,14)],p[0]);c.poly([(22,7),(29,4),(36,7),(40,14),(35,19),(26,19),(21,13)],p[2]);c.rect(23,12,6,3,p[5]);c.rect(34,12,6,3,p[5])
 c.poly([(16,25),(29,20),(43,25),(48,41),(40,50),(20,50),(12,40)],p[0]);c.poly([(20,27),(29,24),(39,27),(43,39),(36,46),(23,45),(17,37)],p[1]);c.poly([(29,27),(37,30),(35,41),(29,44),(24,37)],p[4]);c.put(30,33,p[5])
 c.poly([(15,27),(7,32),(9,43),(17,46),(21,36)],p[0]);c.poly([(44,27),(54,32),(52,43),(44,46),(41,36)],p[0]);c.poly([(20,48),(29,50),(28,61),(21,63),(17,57)],p[0]);c.poly([(36,48),(43,48),(48,58),(43,63),(36,60)],p[0])
 for x,y in [(16,29),(47,30),(20,42),(41,42),(24,52),(39,53)]:c.put(x,y,p[3])
 return c
NEW={
"voidsteel_ingot":"item","embersteel_ingot":"item","starlight_ingot":"item","void_crystal":"item","ember_crystal":"item","starlight_crystal":"item",
"void_sword":"handheld","sun_greatblade":"handheld","moon_dagger":"handheld","aether_spear":"handheld","aether_staff":"handheld",
"void_amulet":"item","sun_ring":"item","moon_talisman":"item","greater_aether_fruit":"item"}
def shaped(pattern,key,result):return {"type":"minecraft:crafting_shaped","pattern":pattern,"key":key,"result":{"item":"isekai:"+result,"count":1}}
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
def main():
 for path in (A/"textures").rglob("*.png"):
  pathimg=armor(path.stem) if "models/armor" in path.as_posix() else sentinel() if "textures/entity" in path.as_posix() else block(path) if "/block/" in path.as_posix() else item(path.stem)
  pathimg.save(path)
 for n,kind in NEW.items():
  item(n).save(A/"textures/item"/(n+".png"))
  (A/"models/item"/(n+".json")).write_text(json.dumps({"parent":"minecraft:item/handheld" if kind=="handheld" else "minecraft:item/generated","textures":{"layer0":"isekai:item/"+n}},indent=2)+"\n")
  (D/"recipes"/(n+".json")).parent.mkdir(parents=True,exist_ok=True)
  (D/"recipes"/(n+".json")).write_text(json.dumps(REC[n],indent=2)+"\n")
 item("aether_crystal").save(A/"icon.png")
 print("Generated",len(list((A/"textures").rglob("*.png"))),"texture PNGs and",len(NEW),"new items")
if __name__=="__main__":main()
