import hashlib
import calendar
import time

def computeHash(user, password):
	md5 = hashlib.md5()
	currentTime = calendar.timegm(time.gmtime()) * 1000
	answer = user + password + str(currentTime)
	md5.update(answer)
	return [user, currentTime, md5.digest().encode('base64').strip()]

user = raw_input("Enter username: ")
password = raw_input("Enter password: ")
print(computeHash(user, password))