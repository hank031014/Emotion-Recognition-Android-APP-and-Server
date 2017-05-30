#!C:\Python\python.exe -u
# -*- coding: utf-8 -*-
import sys, json, time, cgi, cgitb, random
from semantic import *

print("Content-type: application/json")
print("")

form = cgi.FieldStorage() 
text = form["str"].value
emotion_val = int(emotion(text))
message = "沒有偵測到情緒，不好意思！"

fname = "TextToSpeech/emo" + str(emotion_val) + ".txt"
lines = [line.rstrip("\n") for line in open(fname, "r", encoding="UTF-8")]
message = random.choice(lines)	

resp = {}
resp["success"] = 1
resp["emotion_val"] = emotion_val
resp["message"] = message
print(json.dumps(resp))

