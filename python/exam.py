import PyPDF2

def convert_circled_numbers(text):
    circled_to_number = {
        '①': '1', '②': '2', '③': '3', '④': '4', '⑤': '5',
        '⑥': '6', '⑦': '7', '⑧': '8', '⑨': '9', '⑩': '10',
        '⑪': '11', '⑫': '12', '⑬': '13', '⑭': '14', '⑮': '15'
    }
    for circled, number in circled_to_number.items():
        text = text.replace(circled, number)
    return text

def pdf_to_text(pdf_path, txt_path):
    with open(pdf_path, 'rb') as pdf_file:
        pdf_reader = PyPDF2.PdfReader(pdf_file)

        with open(txt_path, 'w', encoding='utf-8') as txt_file:
            for page in pdf_reader.pages:
                text = page.extract_text()
                converted_text = convert_circled_numbers(text)
                txt_file.write(converted_text)

# 사용 예시
pdf_to_text('X:/study/ai/20210814.pdf', 'X:/study/ai/20210814.txt')

# X:\study\ai\gisa\src\main\python>python exam.py

