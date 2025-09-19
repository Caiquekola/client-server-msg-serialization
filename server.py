import socket, json, csv, yaml, toml
import xml.etree.ElementTree as ET
from io import StringIO

host = '127.0.0.1'
port = 9000

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind((host, port))
server.listen(1)
print('Servidor iniciado | {}:{}'.format(host, port))

conn, addr = server.accept()
print('Conectado por: {}'.format(addr))

for _ in range(5):
    data = conn.recv(4096).decode("utf-8")
    formato, conteudo = data.split("\n", 1)
    print(f"\n{formato}")

    if formato == "JSON":
        obj = json.loads(conteudo)
    elif formato == "CSV":
        reader = csv.DictReader(StringIO(conteudo))
        obj = next(reader)
    elif formato == "XML":
        root = ET.fromstring(conteudo)
        obj = {child.tag: child.text for child in root}
    elif formato == "YAML":
        obj = yaml.safe_load(conteudo)
    elif formato == "TOML":
        obj = toml.loads(conteudo)

    print(obj)

conn.close()
server.close()