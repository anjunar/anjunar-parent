package de.industrialsociety.anjunar.timeline;

import de.industrialsociety.common.filedisk.HarddiskFile;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ti_image")
public class TimelineImage extends HarddiskFile { }
