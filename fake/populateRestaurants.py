import pandas as pd
import re
import requests, json

baseUrl = u'http://menufi-192821.appspot.com/'
jsonHeaders = {
  u'Content-Type': u'application/json'
}

def parseSheet(dframe):
  data = {}
  columns = set([column for column, series in dframe.items()])
  for column in columns:
    for index, value in ex_data[column].iteritems():
      if index not in data.keys():
        data[index] = {}
      data[index][column] = value
  return data

def postProcessRestaurants(data):
  for key in data.keys():
    data[key]['name'] = alphanumeric(str(data[key]['name']))

def postProcessItems(data):
  for key in data.keys():
    ingredientsCsv = data[key]['ingredients']
    rawIngredients = ingredientsCsv.split(',')
    data[key]['ingredients'] = list(map(lambda y: y[0].upper() + y[1:].lower(), map(lambda x: x.strip(), rawIngredients)))
  return data

def buildAuthHeader(token):
  authHeaders = dict(jsonHeaders)
  authHeaders[u'Authorization'] = 'MenuFi ' + token
  return authHeaders

def alphanumeric(str):
  return re.sub(r'\W+', '', str)

def getToken(username, password):
  url = baseUrl + u'restaurant/loginToken'
  body = {
    u'email': username,
    u'password': password
  }
  headers = jsonHeaders
  r = requests.post(url=url, data=json.dumps(body), headers=headers)
  content = r.json()
  return content[u'data']

def getRestaurantId(token):
  url = baseUrl + u'restaurants/id'
  headers = buildAuthHeader(token)
  r = requests.get(url=url, headers=headers)
  content = r.json()
  return content[u'data']

def registerRestaurant(username, password):
  url = baseUrl + u'restaurant/registration'
  body = {
    u'email': username,
    u'password': password
  }
  headers = jsonHeaders
  r = requests.post(url=url, data=json.dumps(body), headers=headers)
  content = r.json()
  return content[u'data']

def addRestaurant(name, price, pictureUri, token):
  url = baseUrl + u'restaurants'
  body = {
    u'name': str(name),
    u'price': float(price),
    u'pictureUri': str(pictureUri)
  }
  headers = buildAuthHeader(token)
  r = requests.post(url=url, data=json.dumps(body), headers=headers)
  content = r.json()
  return content[u'data']

def addMenuItem(menuItemDict, restaurantId, token):
  url = baseUrl + u'/restaurants/{}/items'.format(restaurantId)
  body = {
    u'restaurantId': restaurantId,
    u'name': str(menuItemDict['name']),
    u'price': float(menuItemDict['price']),
    u'ingredients': menuItemDict['ingredients'],
    u'dietaryPreferences': [],
    u'calories': int(menuItemDict['calories']),
    u'description': str(menuItemDict['description']),
    u'rating': 0,
    u'pictureUri': str(menuItemDict['pictureUri'])
  }
  headers = buildAuthHeader(token)
  r = requests.post(url=url, data=json.dumps(body), headers=headers)
  content = r.json()
  return content[u'data']

filename = './Menus.xlsx'
sheet_restaurants = 1
sheet_items = 0

ex_data = pd.read_excel('./Menus.xlsx', sheet_name = sheet_restaurants)
restaurants = parseSheet(ex_data)
postProcessRestaurants(restaurants)

ex_data = pd.read_excel('./Menus.xlsx', sheet_name = sheet_items)
items = parseSheet(ex_data)
postProcessItems(items)

# Add all the restaurants
for key in restaurants.keys():
  restaurant = restaurants[key]
  username = '{}@email.com'.format(restaurant['name'])
  password = 'password'
  print('Attempting to register {}, {}'.format(username, password))
  token = ''
  success = registerRestaurant(username, password)
  if success:
    token = getToken(username, password)
    restaurantId = addRestaurant(restaurant['name'], restaurant['price'], '', token)
    print(restaurantId)
    if restaurantId != -1:
      for itemsKey in items.keys():
        item = items[itemsKey]
        if item['restaurantId'] == restaurant['restaurantId']:
          print('Attempting to add [{}]'.format(item['name']))
          itemId = addMenuItem(item, restaurantId, token)
          print(itemId)
  else:
    print('Restaurant already exists')
