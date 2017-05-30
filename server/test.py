#!/usr/bin/python
#-*- coding: utf-8 -*-
from semantic import *
'''
file = open('label/happy.txt', 'r')
dataset = file.read().splitlines()

test = open('label/test.txt', 'w')

i=0
line=len(dataset)
while i<line:
    test.write(str(emotion(dataset[i]))+'\n')
    i += 1



test.close()
file.close()
'''
print(emotion('憤怒沒有人寫個作業叫人家升級別人的程式的拉'))