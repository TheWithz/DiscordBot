import re
import sys
from requests import get


def main():
    args = sys.argv[1:]
    search = '+'.join(args)
    response = get("http://www.bing.com/search", params={'q': search}).text
    match = re.search(r'li class="b_algo.*?href="', response)
    if match is None:
        print('No Results')
        return
    html = response[match.end():]
    match = re.search('"', html)
    html = html[:match.start()]
    print(html)


main()
