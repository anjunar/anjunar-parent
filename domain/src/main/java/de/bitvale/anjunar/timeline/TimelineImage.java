package de.bitvale.anjunar.timeline;

import de.bitvale.common.filedisk.HarddiskFile;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ti_image")
public class TimelineImage extends HarddiskFile { }
