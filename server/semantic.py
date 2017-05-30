#!/usr/bin/python
#-*- coding: utf-8 -*-

from ehownet import *
import jieba
tree=EHowNetTree("db/ehownet_ontology.sqlite")

def emotion(string):
	emotion = [tree.semanticType('MentalState|精神狀態.1'), tree.semanticType('MentalAct|精神動作.1'), tree.semanticType('BeGood|良態.1'), tree.semanticType('BeBad|衰變.1'), tree.semanticType('end|終結.1')]
	emotion_state=0
	negative=0

	seg=jieba.cut(string)
	seg=list(seg)

	vnum=len(seg)
	i=0
	while i<vnum:
		lst=tree.searchWord(seg[i])
		if(len(lst)!=0):
			node=tree.word(lst[0].name)
			categoryList=node.getSemanticTypeList()
			category=tree.semanticType(categoryList[0].name)
			if category.isDescendantOf(emotion[0]) or category.isDescendantOf(emotion[1])or category.isDescendantOf(emotion[2])or category.isDescendantOf(emotion[3]) or category.isDescendantOf(emotion[4]):
				if category.isDescendantOf(tree.semanticType('AtEase|安心.1'))==True:
					emotion_state=1
				elif category.isDescendantOf(tree.semanticType('joyful|喜悅.1')) or category.isDescendantOf(tree.semanticType('AttitudeByGood|好態.1')):
					emotion_state=2
				elif category.isDescendantOf(tree.semanticType('satisfied|滿意.1')) or category.isDescendantOf(tree.semanticType('BeGood|良態.1')):
					emotion_state = 3
				elif category.isDescendantOf(tree.semanticType('FeelNoQualms|無愧.1'))==True:
					emotion_state = 4
				elif category.isDescendantOf(tree.semanticType('shameless|沒羞.1'))==True:
					emotion_state = 5
				elif category.isDescendantOf(tree.semanticType('uneasy|不安.1'))==True:
					emotion_state = 6
				elif category.isDescendantOf(tree.semanticType('unsatisfied|不滿.1'))==True:
					emotion_state = 7
				elif category.isDescendantOf(tree.semanticType('upset|煩惱.1')) or category.isDescendantOf(tree.semanticType('BeBad|衰變.1')):
					emotion_state = 8
				elif category.isDescendantOf(tree.semanticType('sad|憂愁.1')) or category.isDescendantOf(tree.semanticType('AttitudeByBad|壞態.1')):
					emotion_state = 9
				elif category.isDescendantOf(tree.semanticType('sorrowful|悲哀.1'))==True:
					emotion_state = 10
				elif category.isDescendantOf(tree.semanticType('fear|害怕.1')) or category.isDescendantOf(tree.semanticType('end|終結.1')):
					emotion_state = 11
				elif category.isDescendantOf(tree.semanticType('surprise|驚奇.1'))==True:
					emotion_state = 12
				elif category.isDescendantOf(tree.semanticType('worried|著急.1'))==True:
					emotion_state = 13
				elif category.isDescendantOf(tree.semanticType('angry|生氣.1'))==True:
					emotion_state = 14
			elif category.isDescendantOf(tree.semanticType('not.1'))  :
				negative^=1

		i+=1

	if(negative):
		if emotion_state >= 11:
			emotion_state=1
		elif emotion_state >=6:
			emotion_state=3
		elif emotion_state >=4:
			emotion_state=6
		elif emotion_state>=1:
			emotion_state=10

	return emotion_state
