#!/usr/bin/env python

import os
from random import random

def prob(p):
	return random() <= p

with open('ratings.csv', 'r') as f, open('localtest/predictions.csv', 'w') as pred, open('localtest/verification.csv', 'w') as ver, open('localtest/ratings.csv', 'w') as r:
	ratings, k = 0, 0
	for line in f.readlines():
		ratings += 1
		# print line
		if prob(0.25):
			ver.write(line)
			pred.write(line[:line.rfind(';')] + os.linesep)
			k += 1
		else:
			r.write(line)

	print "% of lines in kwart: " + str(k / float(ratings) * 100)
