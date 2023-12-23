# parchment-recipes-web
## Development
### Project Setup
This Python project uses Beautiful Soup 4 and other related packages to parse recipe sites for the title, serving size, preparation time, ingredients, and directions from a given URL link.

To begin, create and activate a virtual environment (venv) and install the following packages:
```
pip install beautifulsoup4
pip install requests
pip install lxml
```
The lxml parser helps nvaigate through HTML errors. From the [Beautiful Soup Documentation](https://beautiful-soup-4.readthedocs.io/en/latest/#installing-a-parser), the lxml parser is fast, lenient, and requires an external C dependency. Alternatively, you can use html5lib.
