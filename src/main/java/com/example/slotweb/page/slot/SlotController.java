package com.example.slotweb.page.slot;

import com.example.slotweb.extend.ExcelSupport;
import com.example.slotweb.service.slot.SlotService;
import com.example.slotweb.service.slot.dto.Slot;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SlotController {

    private final SlotService slotService;


    @PostMapping("/slot/save")
    @ResponseBody
    public ResponseEntity<?> saveMember(@RequestBody Slot.Save saveSlot) {

        try {
            slotService.saveSlot(saveSlot.getSaveSlots(), saveSlot.getDeleteSlotNos());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
