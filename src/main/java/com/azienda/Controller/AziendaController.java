package com.azienda.Controller;

import com.azienda.DTO.AziendaRequestDTO;
import com.azienda.DTO.AziendaRequestPatchDTO;
import com.azienda.DTO.AziendaResponseDTO;
import com.azienda.Service.AziendaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/aziende")
public class AziendaControllerHTML {

    private final AziendaService service;

    public AziendaControllerHTML(AziendaService service) {
        this.service = service;
    }

    @GetMapping
    public String getAll(Model model) {
        List<AziendaResponseDTO> aziende = service.getAll();
        model.addAttribute("aziende", aziende);
        return "index";  // Ritorna la lista delle aziende (index.html)
    }

    @GetMapping("/nuovo")
    public String nuovo() {
        return "aziendaForm";  // Ritorna il template per il form di creazione azienda
    }

    @PostMapping
    public String insert(@ModelAttribute AziendaRequestDTO aziendaRequestDTO) {
        service.insert(aziendaRequestDTO);
        return "redirect:/aziende";  // Redirect alla lista delle aziende dopo l'inserimento
    }

    // Get per la pagina di modifica azienda
    @GetMapping("/modifica/{id}")
    public String modifica(@PathVariable("id") String id, Model model) {
        AziendaResponseDTO azienda = service.getByID(id);
        model.addAttribute("azienda", azienda);
        return "aziendaModifica";  // Template per la modifica
    }

    // Post per aggiornare l'azienda
    @PostMapping("/modifica/{id}")
    public String update(@PathVariable("id") String id, @ModelAttribute AziendaRequestDTO aziendaRequestDTO) {
        service.update(id, aziendaRequestDTO);
        return "redirect:/aziende";  // Redirect alla lista delle aziende
    }

    @GetMapping("/parziale/{id}")
    public String parziale(@PathVariable("id") String id, Model model) {
        AziendaResponseDTO azienda = service.getByID(id);
        model.addAttribute("azienda", azienda);
        return "patch";  // Ritorna il template per il patch
    }

    @PatchMapping("/parziale/{id}")
    public String patch(@PathVariable("id") String id,
                        @RequestParam("campoDaModificare") String campoDaModificare,
                        @RequestParam("nuovoValore") String nuovoValore) {
        // Crea un oggetto DTO per l'aggiornamento parziale
        AziendaRequestPatchDTO patch = new AziendaRequestPatchDTO();

        // Gestisci i tipi di dato in base al campo selezionato
        switch (campoDaModificare) {
            case "nomeAzienda":
                patch.setNomeAzienda(nuovoValore); // Il nome azienda è una stringa
                break;
            case "numeroPersonale":
                try {
                    patch.setNumeroPersonale(Integer.parseInt(nuovoValore)); // Numero personale è un intero
                } catch (NumberFormatException e) {
                    // Gestisci errore di parsing
                    return "redirect:/aziende"; // Ritorna alla lista aziende se c'è errore
                }
                break;
            case "nettoAnnuale":
                try {
                    patch.setNettoAnnuale(Double.parseDouble(nuovoValore)); // Netto annuale è un double
                } catch (NumberFormatException e) {
                    // Gestisci errore di parsing
                    return "redirect:/aziende"; // Ritorna alla lista aziende se c'è errore
                }
                break;
            default:
                // Gestisci eventuali campi non validi
                return "redirect:/aziende"; // Ritorna alla lista aziende se il campo non è valido
        }

        service.updateParziale(id, patch);
        return "redirect:/aziende";  // Redirect alla lista delle aziende dopo l'aggiornamento parziale
    }

    // Metodo per eliminare un'azienda
    @GetMapping("/elimina/{id}")
    public String delete(@PathVariable("id") String id) {
        service.delete(id);
        return "redirect:/aziende";  // Redirect alla lista delle aziende dopo la cancellazione
    }
}
