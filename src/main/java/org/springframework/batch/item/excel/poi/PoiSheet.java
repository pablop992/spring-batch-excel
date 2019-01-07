/*
 * Copyright 2011-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.batch.item.excel.poi;

import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.batch.item.excel.Sheet;

/**
 * Sheet implementation for Apache POI.
 * 
 * @author Marten Deinum
 */
public class PoiSheet implements Sheet {

    private final org.apache.poi.ss.usermodel.Sheet delegate;

    /**
     * Constructor which takes the delegate sheet.
     * 
     * @param delegate the apache POI sheet
     */
    PoiSheet(final org.apache.poi.ss.usermodel.Sheet delegate) {
        super();
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    public int getNumberOfRows() {
        return this.delegate.getLastRowNum() + 1;
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return this.delegate.getSheetName();
    }

    /**
     * {@inheritDoc}
     */
    public String[] getRow(final int rowNumber) {
        if (rowNumber > this.delegate.getLastRowNum()) {
            return null;
        }
        final Row row = this.delegate.getRow(rowNumber);
        final List<String> cells = new LinkedList<>();

        for (Cell cell : row) {
            switch (cell.getCellTypeEnum()) {
                case NUMERIC:
                    cells.add(String.valueOf(cell.getNumericCellValue()));
                    break;
                case BOOLEAN:
                    cells.add(String.valueOf(cell.getBooleanCellValue()));
                    break;
                case STRING:
                case BLANK:
                    cells.add(cell.getStringCellValue());
                    break;
                default:
                    throw new IllegalArgumentException("Cannot handle cells of type '" + cell.getCellTypeEnum() + "'");
            }
        }
        return cells.toArray(new String[0]);
    }

    /**
     * {@inheritDoc}
     */
    public String[] getHeader() {
        return this.getRow(0);
    }

    /**
     * {@inheritDoc}
     */
    public int getNumberOfColumns() {
        final String[] columns = this.getHeader();
        if (columns != null) {
            return columns.length;
        }
        return 0;
    }
}
