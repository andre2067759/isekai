#!/usr/bin/env python3
"""Generate original ISEKAI pixel-art textures using only the Python standard library."""
from pathlib import Path
import struct, zlib

ROOT = Path("src/main/resources/assets/isekai")
PALETTES = {
    "aether": [(8,17,31,255),(17,48,72,255),(23,112,139,255),(45,190,204,255),(135,241,239,255),(231,255,255,255)],
    "moon": [(13,13,34,255),(35,32,75,255),(72,70,137,255),(119,111,201,255),(190,181,246,255),(245,240,255,255)],
    "sun": [(35,18,10,255),(91,45,17,255),(151,83,24,255),(207,137,42,255),(247,195,83,255),(255,239,183,255)],
}
def palette(name):
    return PALETTES["moon" if "moon" in name else "sun" if "sun" in name else "aether"]

class Canvas:
    def __init__(self,w,h):
        self.w,self.h=w,h
        self.p=[(0,0,0,0)]*(w*h)
    def put(self,x,y,c):
        if 0<=x<self.w and 0<=y<self.h: self.p[y*self.w+x]=c
    def rect(self,x,y,w,h,c):
        for yy in range(y,y+h):
            for xx in range(x,x+w): self.put(xx,yy,c)
    def line(self,x0,y0,x1,y1,c,t=1):
        dx, sx=abs(x1-x0),1 if x0<x1 else -1
        dy, sy=-abs(y1-y0),1 if y0<y1 else -1
        err=dx+dy
        while True:
            for yy in range(-t+1,t):
                for xx in range(-t+1,t):
                    if abs(xx)+abs(yy)<t+1: self.put(x0+xx,y0+yy,c)
            if x0==x1 and y0==y1: break
            e=2*err
            if e>=dy: err+=dy; x0+=sx
            if e<=dx: err+=dx; y0+=sy
    def poly(self,pts,c):
        for y in range(min(p[1] for p in pts),max(p[1] for p in pts)+1):
            xs=[]
            for i,(x1,y1) in enumerate(pts):
                x2,y2=pts[(i+1)%len(pts)]
                if (y1<=y<y2) or (y2<=y<y1):
                    xs.append(x1+(y-y1)*(x2-x1)/(y2-y1))
            xs.sort()
            for i in range(0,len(xs)-1,2):
                for x in range(int(xs[i]+0.999),int(xs[i+1])+1): self.put(x,y,c)
    def save(self,path):
        raw=bytearray()
        for y in range(self.h):
            raw.append(0)
            for x in range(self.w):
                raw.extend(self.p[y*self.w+x])
        data=zlib.compress(bytes(raw),9)
        def chunk(kind,payload):
            return struct.pack(">I",len(payload))+kind+payload+struct.pack(">I",zlib.crc32(kind+payload)&0xffffffff)
        png=b"\x89PNG\r\n\x1a\n"
        png+=chunk(b"IHDR",struct.pack(">IIBBBBB",self.w,self.h,8,6,0,0,0))
        png+=chunk(b"IDAT",data)+chunk(b"IEND",b"")
        path.parent.mkdir(parents=True,exist_ok=True); path.write_bytes(png)

def make_block(path):
    c=Canvas(16,16); p=palette(path.name); c.rect(0,0,16,16,p[0])
    for y in range(1,15):
        for x in range(1,15):
            n=(x*7+y*11+(x*y)%9)%13
            c.put(x,y,p[1] if n<4 else p[2] if n<9 else p[0])
    if "ore" in path.name:
        for x,y in [(3,4),(10,3),(7,8),(12,12),(3,12)]:
            c.poly([(x,y-2),(x+2,y-1),(x+2,y+1),(x,y+2),(x-1,y)],p[3])
            c.put(x,y,p[5]); c.put(x+1,y,p[4])
    else:
        c.poly([(2,3),(7,1),(12,4),(10,9),(5,11),(2,8)],p[2])
        c.line(3,4,7,2,p[4]); c.line(7,2,11,5,p[3]); c.line(5,11,10,8,p[1])
    for i in range(16):
        c.put(i,0,p[0]); c.put(i,15,p[0]); c.put(0,i,p[0]); c.put(15,i,p[0])
    return c

def make_item(name,path):
    c=Canvas(16,16); p=palette(name); edge,metal,glow,bright,shine=p[0],p[2],p[3],p[4],p[5]
    if "sword" in name:
        c.line(4,13,12,3,edge,3); c.line(5,12,12,3,metal,2); c.line(6,10,12,3,shine)
        c.line(3,12,7,15,p[1],2); c.line(2,14,5,11,bright); c.put(12,2,shine)
    elif "bow" in name:
        c.line(4,2,8,5,edge,2); c.line(8,5,8,11,metal,2); c.line(8,11,4,14,edge,2)
        c.line(4,2,4,14,shine); c.line(4,8,13,8,bright); c.put(13,8,shine)
    elif any(k in name for k in ("axe","pickaxe","shovel","hoe")):
        c.line(4,13,12,4,edge,3); c.line(4,13,11,5,p[1])
        if "axe" in name: c.poly([(7,4),(12,2),(15,5),(12,9),(8,8)],metal); c.line(8,4,13,4,shine)
        elif "pickaxe" in name: c.line(4,4,13,4,edge,3); c.line(5,4,12,4,shine)
        elif "shovel" in name: c.poly([(9,2),(14,5),(12,9),(8,7)],metal); c.put(11,4,shine)
        else: c.line(6,5,13,5,metal,2); c.line(13,5,14,2,shine)
    elif "helmet" in name:
        c.poly([(2,10),(3,5),(7,2),(12,3),(14,8),(12,12),(3,12)],edge)
        c.poly([(4,9),(5,5),(8,3),(11,4),(12,8),(10,10),(4,10)],metal)
        c.rect(5,8,7,2,glow); c.put(8,6,shine)
    elif "chestplate" in name:
        c.poly([(3,2),(6,3),(8,5),(10,3),(13,2),(15,6),(13,14),(3,14),(1,6)],edge)
        c.poly([(4,4),(7,5),(8,7),(10,5),(13,4),(13,12),(3,12)],metal)
        c.line(8,6,8,12,shine); c.put(8,8,p[5])
    elif "leggings" in name:
        c.poly([(3,2),(13,2),(12,8),(10,14),(7,14),(7,8),(5,14),(2,14)],edge)
        c.rect(4,3,8,4,metal); c.line(8,4,8,13,shine); c.line(4,7,6,12,glow)
    elif "boots" in name:
        c.poly([(3,3),(7,3),(7,10),(13,11),(14,14),(2,14)],edge)
        c.poly([(5,4),(7,5),(7,11),(12,12),(12,13),(4,13)],metal); c.line(5,11,11,12,shine)
    elif "ingot" in name:
        c.poly([(2,6),(5,3),(13,3),(15,6),(12,12),(3,12)],edge)
        c.poly([(4,6),(6,4),(12,4),(13,6),(11,10),(4,10)],metal)
        c.line(5,5,11,5,shine); c.line(4,9,11,9,glow)
    elif any(k in name for k in ("crystal","shard","dust","raw_aether")):
        c.poly([(8,1),(13,6),(11,13),(6,15),(2,9),(4,4)],edge)
        c.poly([(8,2),(12,6),(10,12),(7,13),(4,8),(5,5)],glow)
        c.line(8,2,8,12,shine); c.put(9,5,p[5])
    elif "fruit" in name:
        c.poly([(5,4),(9,3),(13,6),(13,11),(9,14),(4,12),(2,8)],edge)
        c.poly([(5,5),(9,4),(12,7),(11,11),(8,13),(4,11),(3,8)],glow)
        c.line(8,4,10,1,p[1],2); c.put(6,7,shine)
    else:
        c.poly([(3,4),(8,2),(13,5),(14,11),(9,14),(3,12)],edge)
        c.poly([(4,5),(8,3),(12,6),(12,10),(8,12),(4,10)],metal); c.put(8,7,shine)
    c.put(13,2,shine); c.put(2,3,bright)
    return c

def make_armor(name):
    c=Canvas(64,32); p=palette(name)
    # Multiple mapped plates with dark bevels, metallic faces and luminous rune cuts.
    for x,y,w,h in [(0,0,16,16),(20,0,12,16),(40,0,12,16),(0,18,16,14),(20,18,12,14),(40,18,12,14)]:
        c.rect(x+1,y+1,w-2,h-2,p[1]); c.rect(x+2,y+2,w-4,h-4,p[2])
        c.rect(x+3,y+3,w-6,1,p[4]); c.rect(x+3,y+h-4,w-6,1,p[0])
        c.line(x+2,y+2,x+w-3,y+h-3,p[3])
        c.put(x+w//2,y+h//2,p[5])
    return c

def make_sentinel():
    c=Canvas(64,64); p=PALETTES["aether"]; c.rect(0,0,64,64,(8,13,28,255))
    c.poly([(20,5),(28,1),(37,5),(42,14),(37,22),(25,22),(18,15)],p[1])
    c.poly([(23,7),(29,4),(36,7),(39,14),(34,19),(25,18),(21,13)],p[3])
    c.rect(24,12,5,2,p[5]); c.rect(34,12,5,2,p[5])
    c.poly([(17,24),(29,20),(41,24),(46,39),(38,48),(20,47),(13,38)],p[1])
    c.poly([(21,26),(29,23),(37,26),(40,38),(34,44),(23,42),(19,36)],p[2])
    c.poly([(27,27),(34,29),(33,38),(27,40),(24,34)],p[4])
    c.poly([(15,26),(9,31),(11,41),(17,43),(20,35)],p[1]); c.poly([(42,26),(50,30),(49,41),(43,44),(40,35)],p[1])
    c.poly([(21,45),(29,47),(28,59),(22,62),(18,57)],p[1]); c.poly([(35,45),(42,45),(47,57),(42,62),(36,58)],p[1])
    for i in range(12): c.put(8+(i*17)%48,6+(i*11)%52,p[3])
    return c

def main():
    texture_root=ROOT/"textures"
    for path in texture_root.rglob("*.png"):
        name=path.stem
        if "models/armor" in path.as_posix(): image=make_armor(name)
        elif "textures/entity" in path.as_posix(): image=make_sentinel()
        elif "/block/" in path.as_posix(): image=make_block(path)
        else: image=make_item(name,path)
        image.save(path)
    make_item("aether_crystal",ROOT/"icon.png").save(ROOT/"icon.png")
    print("Generated ISEKAI textures:",len(list(texture_root.rglob("*.png")))+1)

if __name__=="__main__": main()
