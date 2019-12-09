package com.example.Sim.Scripts.ScriptGenerator;

import com.example.Sim.Scripts.ScriptGenerator.FunctionCalls.*;
import lombok.Getter;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({AddNpcTraitNode.class, AddPlayerTraitNode.class, ChangeGoldNode.class,
        ChangeNpcNameNode.class,ChangeNpcSkillNode.class,ChangeNpcSkillNode.class,
        ChangePlayerSkillNode.class,ChangePlayerStatNode.class,CheckNpcTraitNode.class ,
        CheckPlayerTraitNode.class,ComparePlayerValueNode.class,CreateNpcNode.class,CreateNpcNode.class,
        HireNpcNode.class,KillNpcNode.class,RemoveNpcTraitNode.class,RemovePlayerTraitNode.class,
        SellNpcNode.class})
@Getter
public abstract class FunctionCall {
    @XmlAttribute
    public String Text ;
    @XmlAttribute
    public String Function ;
    @XmlAttribute
    public String NextNode ;

}
