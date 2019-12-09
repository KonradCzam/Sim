package com.example.Sim.Scripts.ScriptGenerator.FunctionCalls;

import com.example.Sim.Scripts.ScriptGenerator.FunctionCall;
import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


@Getter
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ChangeNpcSkillNode extends FunctionCall {
    @XmlAttribute
    public String NpcName;
    @XmlAttribute
    public String SkillName;
    @XmlAttribute
    public String SkillChange;

    public ChangeNpcSkillNode() {
    }

    public ChangeNpcSkillNode(String text, String nextNode, String npcName, String skillName, String skillChange) {
        this.SkillName = skillName;
        this.SkillChange = skillChange;
        this.NpcName = npcName;
        this.Function = "ChangeNpcSkill";
        this.NextNode = nextNode;
        this.Text = text;
    }

}

