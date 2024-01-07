from bs4 import BeautifulSoup
import requests
import lxml
import re

headers = { ## use headers to prevent blocked  requests
    "User-Agent" : "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
}

## request.get commented to reduce duplicate requests during initial development
##source_page = requests.get('https://www.tastesoflizzyt.com/homemade-cinnamon-rolls/', headers=headers).text ## source_page is now raw html ready for parsing

source_page = open("web.html", "r") ## use local html file of website for parsing during development
soup = BeautifulSoup(source_page, 'lxml') ## use lxml parser

title = soup.find("head").find("title").text ## parse head tag, then find the child title tag, then extract the text content to get recipe name

## exclude site name denoted by special characters surrounded by spaces (ex. cases of recipe name | website name, or recipe name - site name)
title = title.split(" | ")[0].strip() ## split returns a list, take element to parse first part of list with [0]
title = title.split(" - ")[0].strip()
print(title)
## experimenting with re.compile formatting to hand special characters
re_title = title
re_title = re_title.replace('(', r'\(')
re_title = re_title.replace(')', r'\)')
re_title = re_title.replace('-', r'\-')
print(re_title)
print(type(re_title)) ## check type, currently <class str>


titles = soup.find("body") ## parse body, preparing for parsing recipe name
## print body
for title in titles:
   print(title)