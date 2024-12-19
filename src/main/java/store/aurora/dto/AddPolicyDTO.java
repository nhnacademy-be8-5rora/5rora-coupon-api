package store.aurora.dto;

import lombok.Data;

import java.util.List;

@Data
public class AddPolicyDto {
    private List<Long> categoryId;
    private List<Long> bookId;
}
