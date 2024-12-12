package store.aurora.dto;

import lombok.Data;

import java.util.List;

@Data
public class AddPolicyDTO {
    private String policyId;
    private List<Integer> categoryId;
    private List<Integer> bookId;

}
