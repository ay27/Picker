__author__ = 'ns'

import json


def main():
    with open('data/posts/page.0.json', 'r', encoding='utf-8') as fp:
        t = json.loads(fp.read())
        print(t['status'])

main()