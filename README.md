# Calculadora-Veterin-ria
Repositório utilizado para registrar a Calculadora Veterinária, tarefa do Projeto Integrador do Curso de ADS no IFSC

Ligar e desligar machine: 

fly scale count 1 -a calculadora-veterinaria-api
fly machine start 2865666b999768 -a calculadora-vet-db

fly scale count 0 -a calculadora-veterinaria-api
fly machine stop 2865666b999768 ou 
fly machine stop 2865666b999768 -a calculadora-vet-db

Conectar no BD:

fly proxy 5432 -a calculadora-vet-db

message.setFrom("no-reply@calculadora-vet.com"); linha para utilizar o mailtrap no emailservice