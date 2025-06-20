describe('Azienda Controller Cypress Tests', () => {
    let id;

    before(() => {
        cy.visit('http://localhost:8080/aziende');
    });

    it("Recupera la lista delle aziende", () => {
        cy.url().should('include', '/aziende');
    });

    it("Crea una nuova azienda", () => {
        cy.visit('http://localhost:8080/aziende/nuovo');

        cy.get('#nomeAzienda').type('Test Azienda');
        cy.get('#numeroPersonale').type('50');
        cy.get('#PIVA').type('IT1234567890');
        cy.get('#nettoAnnuale').type('100000');

        cy.contains('button', 'Crea Azienda').click();
        cy.url().should('include', '/aziende');
    });

    it("Recupera l'ID della prima azienda e lo salva", () => {
        cy.visit('http://localhost:8080/aziende')
        cy.get('ul li').first().within(() => {
            cy.get('div:first-child span')
              .invoke('text')
              .then((text) => {
                  id = text.trim();
                  cy.log(`ID recuperato: ${id}`);
              });
        });
    });

    it("Modifica un'azienda esistente", () => {
        cy.visit(`http://localhost:8080/aziende/modifica/${id}`);

        cy.get('#nomeAzienda').clear().type('Azienda Modificata Cypress');
        cy.get('#numeroPersonale').clear().type('100');
        cy.get('#nettoAnnuale').clear().type('200000');

        cy.contains('button', 'Aggiorna Azienda').click();
        cy.url().should('include', '/aziende');
    });

    it("Tenta di creare un'azienda con validazione client-side", () => {
        cy.visit('http://localhost:8080/aziende/nuovo');
        cy.get('#nomeAzienda').clear();
        cy.contains('button', 'Crea Azienda').click();
        cy.url().should('include', '/aziende/nuovo');
    });

    it("Tenta di aggiornare un'azienda con dati non validi", () => {
        cy.visit(`http://localhost:8080/aziende/modifica/${id}`);
        cy.get('#nomeAzienda').clear();
        cy.contains('button', 'Aggiorna Azienda').click();
        cy.url().should('include', '/aziende/modifica');
    });

    it("Elimina un'azienda", () => {
        cy.visit(`http://localhost:8080/aziende/elimina/${id}`);

        cy.contains('a', 'Elimina').click();
        cy.on('window:confirm', () => true); // Accetta l'alert di conferma
        
        cy.url().should('include', '/aziende');
    });
});
