import requests
import sys
#jason ='''{
 #        "object_type": "ot" ,
 #        "operation": "op",
 #        "status": "st",
 #        "description": "de",
 #        "project_name": "pn"
 #	 }'''



def sendReq(jason):
	requests.post("http://localhost:8081/integration",data = jason)


if __name__ == "__main__":
	
	filem = open(sys.argv[1])
	
	
	json = filem.read()	

	sendReq(json)
