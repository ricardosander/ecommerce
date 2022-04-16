Sem Broker

```mermaid
flowchart LR;
    A(Navegador) --> B(Servidor HTTP)
    B --> C(Enviar Email Confirmacao)
    C --> L(Log)
    C --> M(Analytics)
    B --> D(Fraude)
    D --> L
    D --> M
    B --> I(Reservar Estoque)
    I --> L
    D --> E(Efetuar Pagamento)
    E --> L
    E --> M
    E --> H(Enviar Email Fracasso)
    H --> L
    E --> K(Cancelar Estoque)
    K --> L
    E --> F(Gerar PDF)
    F --> L
    F --> M
    E --> J(Confirmar Estoque)
    J --> L
    F --> G(Enviar Email PDF)
    G --> L
```
Com Broker

```mermaid
flowchart LR;
    
    subgraph Broker
        C[Broker]
    end
    
    subgraph web
        A[Navegador]
        B[Servidor HTTP]
        
        A --> B
        B --nova compra--> C
    end
    
    subgraph Email
        D[Enviar Email Confirmacao]
        J[Enviar Email Fracasso]
        N[Enviar Email PDF]
    end
    
    subgraph Fraude
        E[Fraude]
    end
    
    subgraph Estoque
        F[Reservar Estoque]
        K[Cancelar Estoque]
        L[Confirmar Estoque]
    end
    
    subgraph Monitoramento
        G[Log]
        H[Analytics]
    end
    
    subgraph Pagamentos
        I[Efetuar Pagamento]
    end
   
    subgraph PDF
        M[Gerar PDF]
    end
    
    
    C --nova compra--> D
    C --nova compra--> E
    C --nova compra--> F
    C --nova compra--> G
    C --nova compra--> H
        
    D --email enviado--> C    
    C --email enviado--> G
    C --email enviado--> H
        
    E --compra sem fraude--> C
    C --compra sem fraude--> G
    C --compra sem fraude--> H
    C --compra sem fraude--> I
        
    E --compra com fraude--> C
    C --compra com fraude--> K
    C --compra com fraude--> G
    C --compra com fraude--> H
        
    I --Pagamento Negado--> C
    C --Pagamento Negado--> J
    C --Pagamento Negado--> K
    C --Pagamento Negado--> G
    C --Pagamento Negado--> H
        
    I --Pagamento Efetuado--> C
    C --Pagamento Efetuado--> L
    C --Pagamento Efetuado--> M
    C --Pagamento Efetuado--> G
    C --Pagamento Efetuado--> H
    
    M --PDF Gerado--> C    
    C --PDF Gerado--> N
    C --PDF Gerado--> G
    C --PDF Gerado--> H
```