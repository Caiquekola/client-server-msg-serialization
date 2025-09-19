import socket, json, csv, yaml, toml, time
from io import StringIO
import xml.etree.ElementTree as ET

host = '127.0.0.1'
port = 9000

dados = {
    "Razao Social": "Universal Software", 
    "Cnpj": "123.456.789.0001-12",
    "Funcionarios": "12",
    "Enderecp": "Belo Horizonte"
}

mensagens = [
    ("CSV", dump_csv(dados)),
    ("JSON", json.dumps(dados)),
    ("XML", dump_xml(dados)),
    ("YAML", yaml.dump(dados)),
    ("TOML", toml.dumps(dados))
]



client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect((host, port))

for formato, msg in mensagens:
    mensagem_final = f"{formato}\n{msg}"
    client.sendall(mensagem_final.encode("utf-8"))
    time.sleep(0.5)

client.close()

def dump_csv(dados: dict) -> str:
    output = StringIO()
    writer = csv.DictWriter(output, fieldnames=dados.keys())
    writer.writeheader()
    writer.writerow(dados)
    return output.getvalue()


def dump_xml(dados: dict) -> str:
    root = ET.Element("Cliente")
    for k, v in dados.items():
        child = ET.SubElement(root, k.replace(" ", ""))  
        child.text = str(v)
    return ET.tostring(root, encoding="unicode")