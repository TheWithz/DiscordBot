import urllib2
import re
import sys

def main():
    args = sys.argv[1:]
    search = '+'.join(args)
    response = urllib2.urlopen(urllib2.Request('http://www.bing.com/search?q='+search))
    html = response.read()
    match = re.search(r'li class="b_algo.*?href="',html)
    if match is None:
        print 'No Results'
        return
    html = html[match.end():]
    match = re.search('"',html)
    html = html[:match.start()]
    print html
    
main()