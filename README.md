# Incêndio

## Introdução
<p align="justify" > O projeto Incêndio será desenvolvido utilizando o framework JADE (Java Agent Development Framework),  software utilizado para o desenvolvimento de agentes inteligentes, implementado em Java, para a disciplina de Paradigmas de Programação UNB-2019-2.
</p>

## Funcionamento

<p align="justify" > Serão criados três agentes, bombeiro, informante e incêndio.  
</p>
<ul>
	<li>Bombeiro: Agente responsável por chegar ao local do incêndio, detectar a intensidade do incêndio e apagá-lo.
	<li> Informante: Responsável por informar ao bombeiro o local do incêndio.
	<li>Incêndio: Agente que gera os incêndios.    
</ul>

Diagrama contendo funcionamento do projeto :

```mermaid
graph LR

A((Incêndio)) 
B((Bombeiro))
C((Informante))
A  -- 1. Envia local incêndio --> C
C  -- 3. envia informação incêndio --> B
B  -- 2. Envia local bombeiro --> C
B  -- 6. Apaga incêndio --> A
A  -- 5. Recebe intensidade --> B
B  -- 4. Pergunta intensidade--> A
