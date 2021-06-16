package de.industrialsociety.anjunar.pages;

import de.industrialsociety.common.filedisk.HarddiskFile;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "PostImage")
@Table(name = "pa_image")
public class PageImage extends HarddiskFile {

    @ManyToOne
    private Page page;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
