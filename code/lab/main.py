# -*- coding: utf-8 -*-
__author__ = 'ns'

from flask import Flask
from flask import render_template, jsonify
from flask import request
import json
app = Flask(__name__)


def get_html(url, req):
    with open(url, 'r', encoding='UTF-8') as fp:
        html = fp.read()
    if req.headers.get('X-Pjax'):
        print('pjax')
        part = html[html.find('<!--**xx'): html.find('<!--xx**')]
        return part
    else:
        return html


@app.route('/')
def index():
    return get_html('index.html', request)


@app.route('/group')
def group_index():
    return get_html('group_index.html', request)


@app.route('/group/<group_id>')
def group(group_id):
    return get_html('group.html', request)


@app.route('/drafts')
def drafts():
    return get_html('drafts.html', request)


@app.route('/enter')
def enter():
    return get_html('enter.html', request)


@app.route('/register', methods=['POST'])
def register():
    print(request.form)
    return 'reg ok'


@app.route('/register/check', methods=['GET'])
def register_check():
    import time
    time.sleep(1)
    if request.args.get('email', '') == 'zhyouns@gmail.com':
        return jsonify({'status': '邮箱已经被使用'})
    if request.args.get('name', '') == 'dick':
        return jsonify({'status': '名字已经被使用'})
    return jsonify({'status': 'success'})


@app.route('/login', methods=['POST'])
def login():
    print(request.form)
    return 'login ok'

@app.route('/new_message_count')
def msg_count():
    import random
    r = {'message': random.randint(0, 12), 'mail':  random.randint(0, 12)}
    return jsonify(r)


@app.route('/full/<pid>')
def get_full(pid):
    req = {}
    print('data/passage/'+pid+'.html')
    with open('data/passage/'+pid+'.html', 'r', encoding='utf-8') as f:
        req['data'] = f.read()
    return jsonify(req)


@app.route('/search')
def search():
    print(request.args)
    return get_html('search.html', request)


@app.route('/json/answer/<pid>/subscribe')
@app.route('/up/<pid>')
def up(pid):
    req = {'status': 'success'}
    return jsonify(req)


@app.route('/json/answer/<pid>/withdraw_subscribe')
@app.route('/cancel_up/<pid>')
def cancel_up(pid):
    req = {'status': 'success'}
    return jsonify(req)


@app.route('/json/question/<pid>/follow')
@app.route('/watch/<pid>')
def watch(pid):
    req = {'status': 'success'}
    return jsonify(req)


@app.route('/json/question/<pid>/withdraw_follow')
@app.route('/cancel_watch/<pid>')
def cancel_watch(pid):
    req = {'status': 'success'}
    return jsonify(req)


@app.route('/question/<pid>/comment')
@app.route('/comment/<pid>')
def get_comment(pid):
    req = {
        'status': 'success',
        'total': 5,
        'comments': [
            {'user_id': 0, 'user_name': '五棵松', 'message': '逻辑学专业作为哲学系的一部分。',
             'photo_link': '/static/images/photo/1.jpg', 'time': '1天前'},
            {'user_id': 1, 'user_name': '维鲁斯饭', 'message': '课表我印象中自己在某个答案中写过但是貌似找不到了。',
             'photo_link': '/static/images/photo/2.jpg', 'time': '2天前'},
            {'user_id': 2, 'user_name': '王师傅', 'message': '一般来说，吹水的课比较好听，但是浪费时间，而干货嘛，稍微一走神就听不懂了。',
             'photo_link': '/static/images/photo/3.jpg', 'time': '3天前'},
            {'user_id': 3, 'user_name': '新欧尚', 'message': '来来来。',
             'photo_link': '/static/images/photo/4.jpg', 'time': '4天前'},
            {'user_id': 4, 'user_name': '汪峰', 'message': '拒绝灌水，从我做起。',
             'photo_link': '/static/images/photo/5.jpg', 'time': '5天前'},
        ]
    }
    return jsonify(req)


@app.route('/message')
def message():
    return get_html('message.html', request)


@app.route('/mail')
def mail():
    return get_html('mail.html', request)


@app.route('/mail_read_flag/<mid>')
def read_flag(mid):
    print('mid:'+mid)
    return jsonify({'status': 'success'})


@app.route('/user/<user_id>')
def user(user_id):
    return get_html('user.html', request)


@app.route('/user/<user_id>/notes/<page>')
def notes(user_id, page):
    # 用户笔记列表的第一页
    return get_html('user.html', request)


@app.route('/json/user/<user_id>/notes/<page>')
@app.route('/user/<user_id>/notes/<page>/get')
def get_notes(user_id, page):
    return jsonify(read_page_json('data/user/notes.json'))


@app.route('/user/<user_id>/questions/<page>')
def questions(user_id, page):
    return get_html('user.html', request)


@app.route('/user/<user_id>/answers/<page>')
def answers(user_id, page):
    return get_html('user.html', request)


@app.route('/json/user/<user_id>/answers/<page>')
@app.route('/user/<user_id>/answers/<page>/get')
def get_answers(user_id, page):
    return jsonify(read_page_json('data/user/answers.json'))


@app.route('/json/user/<user_id>/questions/<page>')
@app.route('/user/<user_id>/questions/<page>/get')
def get_questions(user_id, page):
    return jsonify(read_page_json('data/user/questions.json'))

@app.route('/group_search')
def group_search():
    import time
    time.sleep(1)
    print(request.args)
    return jsonify(read_page_json('data/group/search.json'))


@app.route('/browse/<book_id>/<page>')
def browse(book_id, page):
    return get_html('browse.html', request)


def read_page_json(file_path):
    with open(file_path, 'r', encoding='utf-8') as fp:
        return json.loads(fp.read())


@app.route('/page/<book_id>/<int:start_page>')
def next_page(book_id, start_page):
    import time
    time.sleep(0.5)
    print(request.args.get('filter'))
    print(request.args.get('direction'))
    if start_page == 0:
        r = read_page_json('data/posts/page.0.json')
    elif start_page == 110:
        r = read_page_json('data/posts/page.113.json')
    elif start_page == 114:
        r = read_page_json('data/posts/page.151.json')
    else:
        r = {'status': 'failed'}
    return jsonify(r)


@app.route('/detail/<post_id>')
def detail(post_id):
    return get_html('detail.html', request)


@app.route('/detail/<post_id>/<page>/new', methods=['GET'])
def detail_new(post_id, page):
    return get_html('new.html', request)


@app.route('/detail/<post_id>/<page>/create', methods=['POST'])
def detail_create(post_id, page):
    return str(request.form)


# @app.route('/answer/add/<post_id>', methods=['POST'])
# def add_answer(post_id):
#     print(post_id+' get an new answer')
#     return jsonify({'status': 'success'})


ALLOWED_EXTENSIONS = {'png', 'jpg', 'jpeg', 'gif'}


def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1] in ALLOWED_EXTENSIONS


@app.route('/image_upload', methods=['POST'])
def image_upload():
    image = request.files['image']
    if image and allowed_file(image.filename):
        filename = image.filename
        path = 'static/temp/'+filename
        image.save(path)
        return '{"status": "success", "url": "/%s"}' % path
    return '{"status": "error"}'


@app.route('/json/pmessage/send', methods=['POST'])
def message_receive():
    print('pmessage at: [%s, %s]' % (request.form['receiverId'], request.form['content']))
    return ''


@app.route('/circle/add', methods=['POST'])
def circle_group():
    print('[%s, %s]' % (request.form['name'], request.form['describe']))
    return ''


@app.route('/answer/add/<int:question_id>', methods=['POST'])
def add_answer(question_id):
    print(request.form)
    return ''


if __name__ == '__main__':
    app.debug = True
    app.run(host='0.0.0.0')